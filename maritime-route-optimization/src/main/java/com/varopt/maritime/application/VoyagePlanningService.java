
package com.varopt.maritime.application;

import com.varopt.maritime.domain.*;
import com.varopt.maritime.domain.RouteContext.EnvironmentalForecast;
import com.varopt.maritime.objective.RouteObjective;
import com.varopt.maritime.port.EnvironmentalForecastProvider;
import com.varopt.maritime.port.VesselRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VoyagePlanningService {

    private final VesselRepository vesselRepository;
    private final EnvironmentalForecastProvider forecastProvider;
    private final RouteOptimizer routeOptimizer;
    private final Map<String, RouteObjective> objectives;

    public VoyagePlanningService(
            VesselRepository vesselRepository,
            EnvironmentalForecastProvider forecastProvider,
            RouteOptimizer routeOptimizer,
            java.util.List<RouteObjective> objectiveList
    ) {
        this.vesselRepository = vesselRepository;
        this.forecastProvider = forecastProvider;
        this.routeOptimizer = routeOptimizer;
        this.objectives = objectiveList.stream().collect(
                java.util.stream.Collectors.toUnmodifiableMap(
                        RouteObjective::name,
                        objective -> objective
                )
        );
    }

    public VoyagePlanResult planVoyage(VoyageRequest request) {
        Vessel vessel = vesselRepository.findById(request.vesselId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown vessel: " + request.vesselId()
                ));

        EnvironmentalForecast forecast =
                forecastProvider.getForecast(request);

        String requestedObjective = request.objective() == null
                ? "fuel"
                : request.objective().toLowerCase();

        RouteObjective objective = objectives.get(requestedObjective);

        if (objective == null) {
            throw new IllegalArgumentException(
                    "Unsupported objective: " + requestedObjective
            );
        }

        return routeOptimizer.optimize(
                request, vessel, forecast, objective
        );
    }
}

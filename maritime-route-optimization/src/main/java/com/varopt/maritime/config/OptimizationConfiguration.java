
package com.varopt.maritime.config;

import com.varopt.maritime.domain.Route;
import com.varopt.maritime.domain.RouteContext;
import com.varopt.maritime.domain.VoyageRequest;
import com.varopt.maritime.domain.Vessel;
import com.varopt.maritime.port.EnvironmentalForecastProvider;
import com.varopt.maritime.port.RouteCandidateGenerator;
import com.varopt.maritime.port.VesselRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class OptimizationConfiguration {

    // Demonstration adapter only: replace with a real route generator.
    @Bean
    public RouteCandidateGenerator routeCandidateGenerator() {
        return request -> List.of(
                new Route(
                        "ROUTE-FAST",
                        List.of(new Route.RouteLeg(600, 15))
                ),
                new Route(
                        "ROUTE-EFFICIENT",
                        List.of(new Route.RouteLeg(600, 12))
                )
        );
    }

    // Demonstration adapter only: replace with persistent vessel data.
    @Bean
    public VesselRepository vesselRepository() {
        return vesselId -> Optional.of(
                new Vessel(vesselId, 15, 500)
        );
    }

    // Demonstration adapter only: replace with a forecast API.
    @Bean
    public EnvironmentalForecastProvider forecastProvider() {
        return request -> new RouteContext.EnvironmentalForecast(
                1.0, 1.0
        );
    }
}

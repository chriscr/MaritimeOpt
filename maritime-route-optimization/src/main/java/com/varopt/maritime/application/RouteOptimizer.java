
package com.varopt.maritime.application;

import com.varopt.maritime.domain.*;
import com.varopt.maritime.objective.RouteObjective;
import com.varopt.maritime.port.RouteCandidateGenerator;
import com.varopt.maritime.port.RouteEvaluator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RouteOptimizer {

    private final RouteCandidateGenerator candidateGenerator;
    private final RouteEvaluator routeEvaluator;

    public RouteOptimizer(
            RouteCandidateGenerator candidateGenerator,
            RouteEvaluator routeEvaluator
    ) {
        this.candidateGenerator = candidateGenerator;
        this.routeEvaluator = routeEvaluator;
    }

    public VoyagePlanResult optimize(
            VoyageRequest request,
            Vessel vessel,
            RouteContext.EnvironmentalForecast forecast,
            RouteObjective objective
    ) {
        List<Route> candidates =
                candidateGenerator.generateCandidates(request);

        if (candidates == null || candidates.isEmpty()) {
            return new VoyagePlanResult(
                    "NO_CANDIDATES",
                    objective.name(),
                    List.of(),
                    "No candidate routes were generated."
            );
        }

        List<VoyagePlanResult.ScoredRoute> rankedRoutes =
                candidates.stream()
                        .map(route -> evaluateCandidate(
                                route, vessel, forecast,
                                request.constraints(), objective))
                        .filter(java.util.Objects::nonNull)
                        .sorted(Comparator.comparingDouble(
                                VoyagePlanResult.ScoredRoute::objectiveCost))
                        .toList();

        if (rankedRoutes.isEmpty()) {
            return new VoyagePlanResult(
                    "NO_FEASIBLE_ROUTE",
                    objective.name(),
                    List.of(),
                    "No successfully evaluated feasible route was found."
            );
        }

        return new VoyagePlanResult(
                "SUCCESS",
                objective.name(),
                rankedRoutes,
                "Feasible routes ranked by " + objective.name() + " cost."
        );
    }

    private VoyagePlanResult.ScoredRoute evaluateCandidate(
            Route route,
            Vessel vessel,
            RouteContext.EnvironmentalForecast forecast,
            java.util.Map<String, Double> constraints,
            RouteObjective objective
    ) {
        RouteContext context = new RouteContext(
                route, vessel, forecast, constraints
        );

        RouteEvaluation evaluation = routeEvaluator.evaluate(context);

        // A failed evaluation is not evidence that a route is infeasible.
        if (evaluation.status() != EvaluationStatus.SUCCESS
                || !evaluation.feasible()) {
            return null;
        }

        double objectiveCost = objective.calculateCost(evaluation);

        if (!Double.isFinite(objectiveCost) || objectiveCost < 0) {
            return null;
        }

        return new VoyagePlanResult.ScoredRoute(
                route, evaluation, objectiveCost
        );
    }
}


package com.varopt.maritime.domain;

import java.util.List;

public record VoyagePlanResult(
        String status,
        String objective,
        List<ScoredRoute> rankedRoutes,
        String diagnostic
) {
    public VoyagePlanResult {
        rankedRoutes = rankedRoutes == null
                ? List.of()
                : List.copyOf(rankedRoutes);
    }

    public record ScoredRoute(
            Route route,
            RouteEvaluation evaluation,
            double objectiveCost
    ) {
    }
}

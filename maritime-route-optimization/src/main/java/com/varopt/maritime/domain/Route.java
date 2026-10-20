
package com.varopt.maritime.domain;

import java.util.List;

public record Route(
        String routeId,
        List<RouteLeg> legs
) {
    public Route {
        legs = List.copyOf(legs);
    }

    public record RouteLeg(
            double distanceNauticalMiles,
            double plannedSpeedKnots
    ) {
    }
}

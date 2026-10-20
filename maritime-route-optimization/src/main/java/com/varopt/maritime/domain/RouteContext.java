
package com.varopt.maritime.domain;

import java.util.Map;

public record RouteContext(
        Route route,
        Vessel vessel,
        EnvironmentalForecast forecast,
        Map<String, Double> options
) {
    public RouteContext {
        options = options == null ? Map.of() : Map.copyOf(options);
    }

    public record EnvironmentalForecast(
            double speedFactor,
            double fuelBurnFactor
    ) {
    }
}

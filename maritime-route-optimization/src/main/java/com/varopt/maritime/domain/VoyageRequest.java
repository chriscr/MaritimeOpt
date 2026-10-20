
package com.varopt.maritime.domain;

import java.time.Instant;
import java.util.Map;

public record VoyageRequest(
        String vesselId,
        String origin,
        String destination,
        Instant departureTime,
        Map<String, Double> constraints,
        String objective
) {
    public VoyageRequest {
        constraints = constraints == null
                ? Map.of()
                : Map.copyOf(constraints);
    }
}

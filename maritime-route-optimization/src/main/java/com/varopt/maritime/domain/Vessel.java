
package com.varopt.maritime.domain;

public record Vessel(
        String vesselId,
        double nominalSpeedKnots,
        double fuelBurnLitersPerHour
) {
}


package com.varopt.maritime.domain;

import java.util.List;

public record RouteEvaluation(
        EvaluationStatus status,
        boolean feasible,
        double estimatedFuelLiters,
        double estimatedDurationHours,
        List<ConstraintViolation> violations,
        String diagnostic
) {
    public RouteEvaluation {
        violations = violations == null
                ? List.of()
                : List.copyOf(violations);
    }

    public static RouteEvaluation failed(String diagnostic) {
        return new RouteEvaluation(
                EvaluationStatus.FAILED,
                false,
                0.0,
                0.0,
                List.of(),
                diagnostic
        );
    }
}

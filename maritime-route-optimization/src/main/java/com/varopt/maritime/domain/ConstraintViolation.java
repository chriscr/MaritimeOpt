
package com.varopt.maritime.domain;

public record ConstraintViolation(
        String constraintName,
        String description
) {
}


package com.varopt.maritime.evaluation;

import com.varopt.maritime.domain.ConstraintViolation;
import com.varopt.maritime.domain.Route;
import com.varopt.maritime.domain.RouteContext;
import com.varopt.maritime.domain.RouteEvaluation;
import com.varopt.maritime.domain.EvaluationStatus;
import com.varopt.maritime.port.RouteEvaluator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRouteEvaluator implements RouteEvaluator {

    @Override
    public RouteEvaluation evaluate(RouteContext context) {
        try {
            if (context == null
                    || context.route() == null
                    || context.vessel() == null
                    || context.forecast() == null) {
                return RouteEvaluation.failed(
                        "Route, vessel, and forecast are required."
                );
            }

            var vessel = context.vessel();
            var forecast = context.forecast();

            if (vessel.fuelBurnLitersPerHour() < 0
                    || vessel.nominalSpeedKnots() <= 0
                    || forecast.speedFactor() <= 0
                    || forecast.fuelBurnFactor() < 0) {
                return RouteEvaluation.failed(
                        "Invalid vessel or forecast parameters."
                );
            }

            double totalDurationHours = 0.0;
            double totalFuelLiters = 0.0;
            List<ConstraintViolation> violations = new ArrayList<>();

            for (Route.RouteLeg leg : context.route().legs()) {
                if (leg.distanceNauticalMiles() < 0
                        || leg.plannedSpeedKnots() <= 0) {
                    return RouteEvaluation.failed(
                            "Invalid distance or planned speed."
                    );
                }

                double effectiveSpeed =
                        leg.plannedSpeedKnots() * forecast.speedFactor();

                if (effectiveSpeed <= 0) {
                    return RouteEvaluation.failed(
                            "Effective vessel speed must be positive."
                    );
                }

                double legDurationHours =
                        leg.distanceNauticalMiles() / effectiveSpeed;

                double legFuelLiters =
                        legDurationHours
                        * vessel.fuelBurnLitersPerHour()
                        * forecast.fuelBurnFactor();

                totalDurationHours += legDurationHours;
                totalFuelLiters += legFuelLiters;
            }

            // Illustrative constraint: a maximum voyage duration.
            Double maximumDuration =
                    context.options().get("maxDurationHours");

            if (maximumDuration != null
                    && totalDurationHours > maximumDuration) {
                violations.add(new ConstraintViolation(
                        "MAX_DURATION",
                        "Estimated duration exceeds the allowed limit."
                ));
            }

            return new RouteEvaluation(
                    EvaluationStatus.SUCCESS,
                    violations.isEmpty(),
                    totalFuelLiters,
                    totalDurationHours,
                    violations,
                    "Route evaluation completed."
            );

        } catch (RuntimeException exception) {
            return RouteEvaluation.failed(
                    "Evaluation error: " + exception.getMessage()
            );
        }
    }
}

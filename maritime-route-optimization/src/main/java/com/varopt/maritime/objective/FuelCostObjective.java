
package com.varopt.maritime.objective;

import com.varopt.maritime.domain.RouteEvaluation;
import org.springframework.stereotype.Component;

@Component
public class FuelCostObjective implements RouteObjective {

    @Override
    public String name() {
        return "fuel";
    }

    @Override
    public double calculateCost(RouteEvaluation evaluation) {
        return evaluation.estimatedFuelLiters();
    }
}

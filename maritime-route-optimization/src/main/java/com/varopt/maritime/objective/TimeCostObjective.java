
package com.varopt.maritime.objective;

import com.varopt.maritime.domain.RouteEvaluation;
import org.springframework.stereotype.Component;

@Component
public class TimeCostObjective implements RouteObjective {

    @Override
    public String name() {
        return "time";
    }

    @Override
    public double calculateCost(RouteEvaluation evaluation) {
        return evaluation.estimatedDurationHours();
    }
}

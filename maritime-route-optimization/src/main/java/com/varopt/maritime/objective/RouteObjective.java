
package com.varopt.maritime.objective;

import com.varopt.maritime.domain.RouteEvaluation;

public interface RouteObjective {

    String name();

    double calculateCost(RouteEvaluation evaluation);
}

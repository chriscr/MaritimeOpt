
package com.varopt.maritime.port;

import com.varopt.maritime.domain.RouteContext;
import com.varopt.maritime.domain.RouteEvaluation;

public interface RouteEvaluator {

    RouteEvaluation evaluate(RouteContext context);
}

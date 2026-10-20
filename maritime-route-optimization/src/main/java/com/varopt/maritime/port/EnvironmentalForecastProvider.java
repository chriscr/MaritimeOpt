
package com.varopt.maritime.port;

import com.varopt.maritime.domain.RouteContext.EnvironmentalForecast;
import com.varopt.maritime.domain.VoyageRequest;

public interface EnvironmentalForecastProvider {

    EnvironmentalForecast getForecast(VoyageRequest request);
}

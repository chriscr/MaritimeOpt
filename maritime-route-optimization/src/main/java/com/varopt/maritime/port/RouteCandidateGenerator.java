
package com.varopt.maritime.port;

import com.varopt.maritime.domain.VoyageRequest;
import com.varopt.maritime.domain.Route;

import java.util.List;

public interface RouteCandidateGenerator {

    List<Route> generateCandidates(VoyageRequest request);
}

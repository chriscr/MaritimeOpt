
package com.varopt.maritime.api;

import com.varopt.maritime.application.VoyagePlanningService;
import com.varopt.maritime.domain.VoyagePlanResult;
import com.varopt.maritime.domain.VoyageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voyages")
public class VoyagePlanningController {

    private final VoyagePlanningService planningService;

    public VoyagePlanningController(
            VoyagePlanningService planningService
    ) {
        this.planningService = planningService;
    }

    @PostMapping("/plan")
    public VoyagePlanResult planVoyage(
            @RequestBody VoyageRequest request
    ) {
        return planningService.planVoyage(request);
    }
}


package com.varopt.maritime.port;

import com.varopt.maritime.domain.Vessel;

import java.util.Optional;

public interface VesselRepository {

    Optional<Vessel> findById(String vesselId);
}

package com.jacknie.examples.acl.web.building;

import com.jacknie.examples.acl.jpa.building.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BuildingService {
    Page<Building> getBuildingsPage(BuildingsFilterDto dto, Pageable pageable);

    long createBuilding(SaveBuildingDto dto);

    Optional<Building> findBuilding(long id);

    void updateBuilding(long id, SaveBuildingDto dto);

    void deleteBuilding(long id);
}

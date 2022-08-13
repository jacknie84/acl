package com.jacknie.examples.acl.web.building.facility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BuildingFacilityService {
    Page<BuildingFacilityDto> getBuildingFacilitiesPage(long buildingId, BuildingFacilitiesFilterDto dto, Pageable pageable);

    long createBuildingFacility(long buildingId, SaveBuildingFacilityDto dto);

    Optional<BuildingFacilityDto> findBuildingFacility(long buildingId, long id);

    void updateBuildingFacility(long buildingId, long id, SaveBuildingFacilityDto dto);

    void deleteBuildingFacility(long buildingId, long id);
}

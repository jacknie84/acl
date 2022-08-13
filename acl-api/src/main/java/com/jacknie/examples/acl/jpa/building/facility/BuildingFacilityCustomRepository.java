package com.jacknie.examples.acl.jpa.building.facility;

import com.jacknie.examples.acl.web.building.facility.BuildingFacilitiesFilterDto;
import com.jacknie.examples.acl.web.building.facility.BuildingFacilityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BuildingFacilityCustomRepository {

    Page<BuildingFacilityDto> findAllDto(long buildingId, BuildingFacilitiesFilterDto dto, Pageable pageable);

    Optional<BuildingFacilityDto> findDtoByBuildingIdAndId(long buildingId, long id);
}

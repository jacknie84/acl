package com.jacknie.examples.acl.web.building.facility;

import com.jacknie.examples.acl.jpa.building.Building;
import com.jacknie.examples.acl.jpa.building.BuildingRepository;
import com.jacknie.examples.acl.jpa.building.facility.BuildingFacility;
import com.jacknie.examples.acl.jpa.building.facility.BuildingFacilityRepository;
import com.jacknie.examples.acl.web.HttpStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingFacilityServiceImpl implements BuildingFacilityService {

    private final BuildingRepository buildingRepository;
    private final BuildingFacilityRepository facilityRepository;

    @Override
    public Page<BuildingFacilityDto> getBuildingFacilitiesPage(long buildingId, BuildingFacilitiesFilterDto dto, Pageable pageable) {
        validateBuildingId(buildingId);
        return facilityRepository.findAllDto(buildingId, dto, pageable);
    }

    @Override
    public long createBuildingFacility(long buildingId, SaveBuildingFacilityDto dto) {
        Building building = getBuilding(buildingId);
        BuildingFacility.BuildingFacilityBuilder builder = BuildingFacility.builder()
            .building(building)
            .name(dto.getName());
        Optional.ofNullable(dto.getParentId())
            .map(parentId -> facilityRepository.findById(parentId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.BAD_REQUEST)))
            .ifPresent(builder::parent);
        return facilityRepository.save(builder.build()).getId();
    }

    @Override
    public Optional<BuildingFacilityDto> findBuildingFacility(long buildingId, long id) {
        return facilityRepository.findDtoByBuildingIdAndId(buildingId, id);
    }

    @Override
    public void updateBuildingFacility(long buildingId, long id, SaveBuildingFacilityDto dto) {
        BuildingFacility entity = facilityRepository.findByBuildingIdAndId(buildingId, id)
            .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND));
        entity.setName(dto.getName());
        facilityRepository.save(entity);
    }

    @Override
    public void deleteBuildingFacility(long buildingId, long id) {
        BuildingFacility entity = facilityRepository.findByBuildingIdAndId(buildingId, id)
            .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND));
        facilityRepository.delete(entity);
    }

    private void validateBuildingId(long buildingId) {
        if (!buildingRepository.existsById(buildingId)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private Building getBuilding(long buildingId) {
        return buildingRepository.findById(buildingId).orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND));
    }

}

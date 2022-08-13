package com.jacknie.examples.acl.web.building.facility;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/buildings/{buildingId}/facilities")
@RequiredArgsConstructor
public class BuildingFacilityController {

    private final BuildingFacilityService facilityService;

    @GetMapping
    public ResponseEntity<?> getBuildingFacilitiesPage(
        @PathVariable("buildingId") long buildingId,
        @Valid BuildingFacilitiesFilterDto dto, Pageable pageable
    ) {
        Page<BuildingFacilityDto> page = facilityService.getBuildingFacilitiesPage(buildingId, dto, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<?> postBuildingFacility(
        @PathVariable("buildingId") long buildingId,
        @RequestBody @Valid SaveBuildingFacilityDto dto,
        UriComponentsBuilder uri
    ) {
        long id = facilityService.createBuildingFacility(buildingId, dto);
        URI location = uri.path("/buildings/{buildingId}/facilities/{id}").buildAndExpand(buildingId, id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingFacility(
        @PathVariable("buildingId") long buildingId,
        @PathVariable("id") long id
    ) {
        return ResponseEntity.of(facilityService.findBuildingFacility(buildingId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putBuildingFacility(
        @PathVariable("buildingId") long buildingId,
        @PathVariable("id") long id,
        @RequestBody @Valid SaveBuildingFacilityDto dto
    ) {
        facilityService.updateBuildingFacility(buildingId, id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuildingFacility(
        @PathVariable("buildingId") long buildingId,
        @PathVariable("id") long id
    ) {
        facilityService.deleteBuildingFacility(buildingId, id);
        return ResponseEntity.noContent().build();
    }
}

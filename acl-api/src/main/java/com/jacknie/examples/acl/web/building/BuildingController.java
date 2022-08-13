package com.jacknie.examples.acl.web.building;

import com.jacknie.examples.acl.jpa.building.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping
    public ResponseEntity<?> getBuildingsPage(@Valid BuildingsFilterDto dto, Pageable pageable) {
        Page<Building> page = buildingService.getBuildingsPage(dto, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<?> createBuilding(@RequestBody @Valid SaveBuildingDto dto, UriComponentsBuilder uri) {
        long id = buildingService.createBuilding(dto);
        URI location = uri.path("/buildings/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuilding(@PathVariable("id") long id) {
        return ResponseEntity.of(buildingService.findBuilding(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putBuilding(@PathVariable("id") long id, @RequestBody @Valid SaveBuildingDto dto) {
        buildingService.updateBuilding(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuilding(@PathVariable("id") long id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}

package com.jacknie.examples.acl.web.building;

import com.jacknie.examples.acl.jpa.building.Building;
import com.jacknie.examples.acl.jpa.building.BuildingRepository;
import com.jacknie.examples.acl.web.HttpStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;

    @Override
    public Page<Building> getBuildingsPage(BuildingsFilterDto dto, Pageable pageable) {
        return buildingRepository.findAll(dto, pageable);
    }

    @Override
    public long createBuilding(SaveBuildingDto dto) {
        Building entity = Building.builder()
            .name(dto.getName())
            .build();
        return buildingRepository.save(entity).getId();
    }

    @Override
    public Optional<Building> findBuilding(long id) {
        return buildingRepository.findById(id);
    }

    @Override
    public void updateBuilding(long id, SaveBuildingDto dto) {
        Building entity = buildingRepository.findById(id)
            .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND));
        entity.setName(dto.getName());
        buildingRepository.save(entity);
    }

    @Override
    public void deleteBuilding(long id) {
        try {
            buildingRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, e);
        }
    }
}

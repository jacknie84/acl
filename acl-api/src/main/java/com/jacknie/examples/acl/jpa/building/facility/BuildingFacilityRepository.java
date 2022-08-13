package com.jacknie.examples.acl.jpa.building.facility;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingFacilityRepository extends JpaRepository<BuildingFacility, Long>, BuildingFacilityCustomRepository {
    Optional<BuildingFacility> findByBuildingIdAndId(long buildingId, long id);
}

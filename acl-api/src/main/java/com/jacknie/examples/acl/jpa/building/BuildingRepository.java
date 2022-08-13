package com.jacknie.examples.acl.jpa.building;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long>, BuildingCustomRepository {
}

package com.jacknie.examples.acl.jpa.building;

import com.jacknie.examples.acl.web.building.BuildingsFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuildingCustomRepository {

    Page<Building> findAll(BuildingsFilterDto dto, Pageable pageable);
}

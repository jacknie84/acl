package com.jacknie.examples.acl.web.building.facility;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.Instant;

@Data
public class BuildingFacilityDto {

    private Long id;

    private String name;

    private Long parentId;

    private Long buildingId;

    private Instant lastModifiedDate;

    @QueryProjection
    public BuildingFacilityDto(Long id, String name, Long parentId, Long buildingId, Instant lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.buildingId = buildingId;
        this.lastModifiedDate = lastModifiedDate;
    }
}

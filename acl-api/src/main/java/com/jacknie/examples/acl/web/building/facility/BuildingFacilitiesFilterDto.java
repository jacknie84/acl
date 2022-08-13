package com.jacknie.examples.acl.web.building.facility;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class BuildingFacilitiesFilterDto {

    private String search;

    @Positive
    private Long parentId;

    private boolean parentIdIsNull;
}

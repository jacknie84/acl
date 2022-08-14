package com.jacknie.examples.acl.web.building.facility;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
public class BuildingFacilitiesFilterDto {

    private String search;

    @Positive
    private Long parentId;

    private boolean nullParentId;

    private Set<@NotNull @Positive Long> ids;
}

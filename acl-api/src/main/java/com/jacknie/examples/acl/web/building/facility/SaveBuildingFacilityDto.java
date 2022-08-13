package com.jacknie.examples.acl.web.building.facility;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class SaveBuildingFacilityDto {

    @Positive
    private Long parentId;

    @NotBlank
    @Length(max = 100)
    private String name;

}

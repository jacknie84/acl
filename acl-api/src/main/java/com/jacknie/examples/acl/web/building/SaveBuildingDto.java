package com.jacknie.examples.acl.web.building;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class SaveBuildingDto {

    @NotBlank
    @Length(max = 100)
    private String name;
}

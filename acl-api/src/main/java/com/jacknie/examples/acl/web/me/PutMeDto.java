package com.jacknie.examples.acl.web.me;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class PutMeDto {

    @NotBlank
    @Length(min = 6, max = 15)
    private String orgPassword;

    @NotBlank
    @Length(min = 6, max = 15)
    private String newPassword;
}

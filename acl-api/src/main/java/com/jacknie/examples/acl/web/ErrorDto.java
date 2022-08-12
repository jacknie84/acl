package com.jacknie.examples.acl.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private Object code;
    private String message;
}

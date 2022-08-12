package com.jacknie.examples.acl.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@Getter
public class HttpStatusException extends RuntimeException {

    private final HttpStatus httpStatus;

    public HttpStatusException(HttpStatus httpStatus) {
        this(httpStatus, (String) null);
    }

    public HttpStatusException(HttpStatus httpStatus, @Nullable String message) {
        super(StringUtils.hasText(message) ? message : httpStatus.toString());
        this.httpStatus = httpStatus;
    }

    public HttpStatusException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }
}

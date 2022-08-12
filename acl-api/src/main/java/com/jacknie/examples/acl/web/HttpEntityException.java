package com.jacknie.examples.acl.web;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@Getter
public class HttpEntityException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final HttpEntity<?> httpEntity;

    public HttpEntityException(HttpStatus httpStatus, HttpEntity<?> httpEntity) {
        this(httpStatus, httpEntity, (String) null);
    }

    public HttpEntityException(HttpStatus httpStatus, HttpEntity<?> httpEntity, @Nullable String message) {
        super(StringUtils.hasText(message) ? message : httpStatus.toString());
        this.httpStatus = httpStatus;
        this.httpEntity = httpEntity;
    }

    public HttpEntityException(HttpStatus httpStatus, HttpEntity<?> httpEntity, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
        this.httpEntity = httpEntity;
    }
}

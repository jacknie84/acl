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

    public static class NotFound extends HttpStatusException {

        public NotFound() {
            super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.toString());
        }

        public NotFound(@Nullable String message) {
            super(HttpStatus.NOT_FOUND, message);
        }

        public NotFound(Throwable cause) {
            super(HttpStatus.NOT_FOUND, cause);
        }
    }
}

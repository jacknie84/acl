package com.jacknie.examples.acl.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<?> handleException(HttpStatusException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(HttpEntityException.class)
    public ResponseEntity<?> handleException(HttpEntityException e) {
        HttpEntity<?> httpEntity = e.getHttpEntity();
        return ResponseEntity.status(e.getHttpStatus())
            .headers(httpEntity.getHeaders())
            .body(httpEntity.getBody());
    }
}

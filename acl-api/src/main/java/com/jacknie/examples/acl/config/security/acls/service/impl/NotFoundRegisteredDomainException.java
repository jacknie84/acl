package com.jacknie.examples.acl.config.security.acls.service.impl;

public class NotFoundRegisteredDomainException extends RuntimeException {

    private NotFoundRegisteredDomainException(String message) {
        super(message);
    }

    public static NotFoundRegisteredDomainException byType(Class<?> type) {
        String format = "객체 클래스(%s)로 등록 된 도메인 객체를 찾을 수 없습니다.";
        String message = String.format(format, type);
        return new NotFoundRegisteredDomainException(message);
    }

    public static NotFoundRegisteredDomainException byCode(String code) {
        String format = "도메인 코드(%s)로 등록 된 도메인 객체를 찾을 수 없습니다.";
        String message = String.format(format, code);
        return new NotFoundRegisteredDomainException(message);
    }
}

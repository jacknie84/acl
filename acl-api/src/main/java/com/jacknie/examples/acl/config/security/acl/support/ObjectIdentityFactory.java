package com.jacknie.examples.acl.config.security.acl.support;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import org.springframework.security.acls.model.ObjectIdentity;

import java.io.Serializable;

public interface ObjectIdentityFactory {

    /**
     * 객체 식별 정보 생성
     * @param target 대상 객체
     * @return 객체 식별 정보
     */
    ObjectIdentity createObjectIdentity(AclIdentifiable target);

    /**
     * 객체 식별 정보 생성
     * @param identifier 객체 식별 정보
     * @param domainCode 도메인 코드
     * @return 객체 식별 정보
     */
    ObjectIdentity createObjectIdentity(Serializable identifier, String domainCode);
}

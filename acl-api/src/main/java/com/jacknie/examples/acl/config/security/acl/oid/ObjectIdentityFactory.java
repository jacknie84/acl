package com.jacknie.examples.acl.config.security.acl.meta.oid;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import com.jacknie.examples.acl.jpa.acl.oid.AclObject;
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

    /**
     * 객체 식별 정보 생성
     * @param aclObject ACL 객체 생성
     * @return 객체 식별 정보
     */
    ObjectIdentity createObjectIdentity(AclObject aclObject);
}

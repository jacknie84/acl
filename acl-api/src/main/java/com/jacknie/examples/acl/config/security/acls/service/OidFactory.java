package com.jacknie.examples.acl.config.security.acls.service;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.config.security.acls.domain.OidModel;
import com.jacknie.examples.acl.jpa.acl.oid.AclOid;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

public interface OidFactory extends ObjectIdentityRetrievalStrategy, ObjectIdentityGenerator {

    /**
     * 객체 식별 정보 생성
     * @param oid 객체 식별 정보 엔터티
     * @return 객체 식별 정보
     */
    ObjectIdentity createObjectIdentity(AclOid oid);

    /**
     * 객체 식별 정보 생성
     * @param identifiable 식별 가능한 객체
     * @return 객체 식별 정보
     */
    ObjectIdentity createObjectIdentity(AclIdentifiable identifiable);

    /**
     * 객체 식별 정보 생성
     * @param oidModel 객체 식별 정보 데이터
     * @return 객체 식별 정보
     */
    ObjectIdentity createObjectIdentity(OidModel oidModel);
}

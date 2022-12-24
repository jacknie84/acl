package com.jacknie.examples.acl.config.security.acls.service;

import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;

public interface AclConstantService {

    /**
     * 도메인 코드에 해당 하는 ACL 상수 정보 조회
     * @param domainCode 도메인 코드
     * @return ACL 상수 정보
     */
    AclConstant getOrCreateDomain(String domainCode);

    /**
     * 루트 코드에 해당 하는 ACL 상수 정보 조회
     * @param rootCode 루트 코드
     * @return ACL 상수 정보
     */
    AclConstant getOrCreateRoot(String rootCode);
}

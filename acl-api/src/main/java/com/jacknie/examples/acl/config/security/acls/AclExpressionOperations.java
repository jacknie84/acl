package com.jacknie.examples.acl.config.security.acls;

public interface AclExpressionOperations {

    /**
     * 인증 객체에 도메인 객체를 생성할 수 있는 권한이 있는지 확인
     * @param domainCode 도메인 코드
     * @return 권한 여부
     */
    boolean hasCreatePermission(String domainCode);
}

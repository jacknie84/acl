package com.jacknie.examples.acl.config.security.acls.hierarchy;

import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;

public interface AclHierarchyKeeper {

    /**
     * 해당 ACL 상수 정보를 바탕으로 초기화 처리
     * @param constant ACL 상수 정보
     */
    void initialize(AclConstant constant);
}

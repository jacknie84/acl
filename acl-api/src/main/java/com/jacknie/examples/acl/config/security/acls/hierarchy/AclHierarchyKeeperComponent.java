package com.jacknie.examples.acl.config.security.acls.hierarchy;

import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;
import org.springframework.security.acls.model.MutableAcl;

public interface AclHierarchyKeeperComponent {

    /**
     * ACL 상수 정보에 대한 지원 여부 확인
     * @param constant ACL 상수 정보
     * @return 지원 여부
     */
    boolean isSupport(AclConstant constant);

    /**
     * 권한 목록 유지
     * @param constant ACL 상수 정보
     * @return ACL 객체
     */
    MutableAcl keepAces(AclConstant constant);
}

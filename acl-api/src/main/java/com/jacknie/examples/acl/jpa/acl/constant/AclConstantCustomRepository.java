package com.jacknie.examples.acl.jpa.acl.constant;

import java.util.Optional;

public interface AclConstantCustomRepository {

    /**
     * 메타 정보에 대한 ACL 상수 정보 조회
     * @param meta 메타 정보
     * @return ACL 상수 정보
     */
    Optional<AclConstant> findOne(AclConstantMeta meta);
}

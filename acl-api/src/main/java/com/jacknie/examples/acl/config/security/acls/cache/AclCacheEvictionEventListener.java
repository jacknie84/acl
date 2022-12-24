package com.jacknie.examples.acl.config.security.acls.cache;

import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;

import java.io.Serializable;

public interface AclCacheEvictionEventListener {

    /**
     * ACL 캐시 제거 이벤트 핸들링
     * @param pk 식별 키
     * @param oid 객체 식별 정보
     */
    void onEvict(Serializable pk, ObjectIdentity oid);
}

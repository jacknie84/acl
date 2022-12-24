package com.jacknie.examples.acl.config.security.acls.service;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Sid;

public interface AclServiceTemplate {

    /**
     * ACL 을 조회 하거나, 없으면 생성해서 제공
     * @param identifiable 대상 도메인 객체
     * @param owner 소유자 보안 식별 정보
     * @param parent 부모 ACL
     * @return ACL 객체
     */
    MutableAcl getOrCreateAcl(AclIdentifiable identifiable, Sid owner, @Nullable Acl parent);

    /**
     * ACL 객체에 권한을 추가 처리
     * @param params ACE 추가 처리 매개 변수
     */
    void appendAce(AppendAceParams params);
}

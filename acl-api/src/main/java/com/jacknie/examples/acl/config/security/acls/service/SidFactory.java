package com.jacknie.examples.acl.config.security.acls.service;

import com.jacknie.examples.acl.config.security.acls.domain.SidModel;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.GrantedAuthority;

public interface SidFactory extends SidRetrievalStrategy {

    /**
     * 보안 식별 정보를 생성
     * @param sidModel 보안 식별 정보 데이터
     * @return 보안 식별 정보
     */
    Sid createSid(SidModel sidModel);

    /**
     * 보안 식별 정보를 생성
     * @param grantedAuthority 권한 정보
     * @return 보안 식별 정보
     */
    Sid createSid(GrantedAuthority grantedAuthority);
}

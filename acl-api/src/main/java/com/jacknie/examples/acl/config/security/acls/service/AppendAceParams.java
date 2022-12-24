package com.jacknie.examples.acl.config.security.acls.service;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

@Data
@Builder
public class AppendAceParams {

    /**
     * ACL 객체
     */
    private final MutableAcl acl;

    /**
     * 권한
     */
    private final Permission permission;

    /**
     * 권한을 갖는 보안 식별 정보
     */
    private final Sid sid;

    /**
     * 권한 여부 처리 시 성공 로그 여부
     */
    private final Boolean auditSuccess;

    /**
     * 권한 여부 처리 시 실패 로그 여부
     */
    private final Boolean auditFailure;
}

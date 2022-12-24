package com.jacknie.examples.acl.jpa.acl.oid;

import com.jacknie.examples.acl.config.security.acls.domain.SidType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AclSourceBasePart {

    /**
     * ACL 객체 아이디
     */
    private final Long aclObjectId;

    /**
     * 부모 ACL 객체 아이디
     */
    private final Long parentAclObjectId;

    /**
     * 부모의 ACE 목록이 ACL 로 부터 상속 여부
     */
    private final Boolean entriesInheriting;

    /**
     * 객체 식별 정보
     */
    private final String objectIdentifier;

    /**
     * 도메인 아이디 변형 타입
     */
    private final String domainIdConverter;

    /**
     * 도메인 코드
     */
    private final String domainCode;

    /**
     * SID 타입
     */
    private final SidType sidType;

    /**
     * SID 값
     */
    private final String sidValue;

    @QueryProjection
    public AclSourceBasePart(Long aclObjectId, Long parentAclObjectId, Boolean entriesInheriting, String objectIdentifier, String domainIdConverter, String domainCode, SidType sidType, String sidValue) {
        this.aclObjectId = aclObjectId;
        this.parentAclObjectId = parentAclObjectId;
        this.entriesInheriting = entriesInheriting;
        this.objectIdentifier = objectIdentifier;
        this.domainIdConverter = domainIdConverter;
        this.domainCode = domainCode;
        this.sidType = sidType;
        this.sidValue = sidValue;
    }
}

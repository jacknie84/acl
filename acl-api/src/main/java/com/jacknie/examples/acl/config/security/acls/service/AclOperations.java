package com.jacknie.examples.acl.config.security.acls.service;

import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import java.io.Serializable;
import java.util.List;

public interface AclOperations extends LookupOperations {

    /**
     * 해당 객체 식별 정보가 관리 되고 있는지 확인
     * @param oid 객체 식별 정보
     * @return 관리 여부
     */
    boolean existsObjectIdentity(ObjectIdentity oid);

    /**
     * Creates an entry in the acl_object_identity table for the passed ObjectIdentity.
     * The Sid is also necessary, as acl_object_identity has defined the sid column as
     * non-null.
     * @param oid to represent an acl_object_identity for
     * @param owner for the SID column (will be created if there is no acl_sid entry for
     * this particular Sid already)
     */
    default void createObjectIdentity(ObjectIdentity oid, Sid owner) {
        createObjectIdentity(oid, owner, null);
    }

    /**
     * acl_object 테이블에 객체 식별 정보에 해당하는 레코드 생성
     * @param oid 객체 식별 정보
     * @param owner 객체 소유자 정보
     * @param parentId 부모 ACL 아이디
     */
    void createObjectIdentity(ObjectIdentity oid, Sid owner, @Nullable Serializable parentId);

    /**
     * ACL 삭제 처리
     * @param oid 객체 식별 정보
     * @param deleteChildren 자식 삭제 처리 여부
     */
    void deleteAcl(ObjectIdentity oid, boolean deleteChildren);

    /**
     * ACL 수정 처리
     * @param acl ACL 객체
     */
    void updateAcl(MutableAcl acl);

    /**
     * 객체 식별 정보의 자식 목록을 조회
     * @param oid 객체 식별 정보
     * @return 자식 목록
     */
    List<ObjectIdentity> findChildren(ObjectIdentity oid);

}

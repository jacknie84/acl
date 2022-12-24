package com.jacknie.examples.acl.jpa.acl.oid;

import com.jacknie.examples.acl.web.acl.AclFilterDto;
import com.jacknie.examples.acl.web.member.acl.MemberAclFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import java.util.*;

public interface AclObjectIdentityCustomRepository {

    /**
     * 객체 식별 정보가 검색 되는지 확인
     * @param oid 객체 식별 정보
     * @return 검색 여부
     */
    boolean existsByObjectIdentity(ObjectIdentity oid);

    /**
     * ACL 소스 데이터 기본 정보 목록 조회
     * @param oids 객체 식별 정보 목록
     * @return 기본 정보 목록
     */
    List<AclSourceBasePart> findAclSourceBasePartsByObjectIdentityIn(Set<ObjectIdentity> oids);

    /**
     * ACL 소스 데이터 기본 정보 목록 조회
     * @param ids 객체 식별 정보 아이디 목록
     * @return 기본 정보 목록
     */
    List<AclSourceBasePart> findAclSourceBasePartsByObjectIdentityIdIn(Set<Long> ids);

    /**
     * 객체 식별 정보 조회
     * @param oid 객체 식별 정보
     * @return 객체 식별 정보
     */
    Optional<AclObjectIdentity> findByObjectIdentity(ObjectIdentity oid);

    /**
     * 객체 식별 정보 맵 조회
     * @param oidIds 객체 식별 정보 아이디
     * @return 객체 식별 정보 맵
     */
    Map<Long, AclObjectIdentity> findMapByIdIn(Set<Long> oidIds);

    /**
     * 객체 식별 정보 아이디 조회
     * @param oid 객체 식별 정보
     * @return 객체 식별 정보 아이디
     */
    Optional<Long> findIdByObjectIdentity(ObjectIdentity oid);

    /**
     * 객체 식별 정보 소스 데이터 자식 목록 조회
     * @param oid 부모 객체 식별 정보
     * @return 객체 식별 정보 소스 데이터 자식 목록
     */
    List<ObjectIdentitySource> findChildrenByObjectIdentity(ObjectIdentity oid);

    Page<AclObjectIdentity> findAll(AclFilterDto dto, Pageable pageable);

    List<AclObjectIdentity> findAll(List<Sid> sids, MemberAclFilterDto dto);

    List<AclObjectIdentity> findAllByEntrySid(Sid sid);

    List<AclObjectIdentity> findAllBySidIn(List<Sid> sids);
}

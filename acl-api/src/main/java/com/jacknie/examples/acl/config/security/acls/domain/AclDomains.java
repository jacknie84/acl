package com.jacknie.examples.acl.config.security.acls.domain;

import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;
import com.jacknie.experimental.acl.oid.AclDomainMeta;
import com.jacknie.examples.acl.jpa.building.Building;
import com.jacknie.examples.acl.jpa.building.member.BuildingMember;
import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.web.building.facility.BuildingFacilityDto;
import com.jacknie.examples.acl.web.member.account.MemberAccountDto;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public enum AclDomains implements AclDomainMeta {

    /**
     * ACL 상수 도메인
     */
    CONSTANT(AclDomainCodes.CONSTANT, AclConstant.class),

    /**
     * 메뉴 도메인
     */
    MENU(AclDomainCodes.MENU, Menu.class),

    /**
     * 회원 도메인
     */
    MEMBER(AclDomainCodes.MEMBER, MemberAccount.class, MemberAccountDto.class),

    /**
     * 건물 도메인
     */
    BUILDING(AclDomainCodes.BUILDING, Building.class),

    /**
     * 건물 회원 도메인
     */
    BUILDING_MEMBER(AclDomainCodes.BUILDING_MEMBER, BuildingMember.class),

    /**
     * 건물 시설 도메인
     */
    BUILDING_FACILITY(AclDomainCodes.BUILDING_FACILITY, BuildingFacilityDto.class);

    private final String code;
    private final Set<Class<? extends AclIdentifiable>> types;

    @SafeVarargs
    AclDomains(String code, Class<? extends AclIdentifiable>... types) {
        Arrays.stream(types).forEach(Objects::requireNonNull);
        this.code = Objects.requireNonNull(code);
        this.types = Set.of(types);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Set<Class<? extends AclIdentifiable>> getTypes() {
        return types;
    }
}

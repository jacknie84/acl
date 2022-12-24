package com.jacknie.examples.acl.jpa.building.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BuildingMemberId implements Serializable {

    /**
     * 회원 계정 아이디
     */
    private Long accountId;

    /**
     * 건물 아이디
     */
    private Long buildingId;
}

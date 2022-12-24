package com.jacknie.examples.acl.jpa.building.member;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.jpa.building.Building;
import com.jacknie.examples.acl.jpa.member.MemberAccount;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "building_member")
public class BuildingMember implements AclIdentifiable {

    /**
     * 아이디
     */
    @EmbeddedId
    private BuildingMemberId id;

    /**
     * 회원 계정 정보
     */
    @MapsId("accountId")
    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private MemberAccount memberAccount;

    /**
     * 건물 정보
     */
    @MapsId("buildingId")
    @ManyToOne
    @JoinColumn(name = "buildingId", referencedColumnName = "id")
    private Building building;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BuildingMember that = (BuildingMember) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

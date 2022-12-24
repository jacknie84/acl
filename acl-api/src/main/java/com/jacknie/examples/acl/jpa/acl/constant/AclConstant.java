package com.jacknie.examples.acl.jpa.acl.constant;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.jpa.building.Building;
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
@Table(name = "acl_constant", uniqueConstraints = @UniqueConstraint(columnNames = { "metaType", "metaCode", "buildingId" }))
public class AclConstant implements AclIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "metaType", nullable = false)),
        @AttributeOverride(name = "code", column = @Column(name = "metaCode", nullable = false)),
    })
    private AclConstantMeta meta;

    @ManyToOne
    @JoinColumn(name = "buildingId", referencedColumnName = "id")
    private Building building;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AclConstant that = (AclConstant) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

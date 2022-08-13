package com.jacknie.examples.acl.jpa.building.facility;

import com.jacknie.examples.acl.jpa.building.Building;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BuildingFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private BuildingFacility parent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buildingId", referencedColumnName = "id", nullable = false)
    private Building building;

    @LastModifiedDate
    @Column(nullable = false)
    @ColumnDefault("current_timestamp")
    private Instant lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BuildingFacility that = (BuildingFacility) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

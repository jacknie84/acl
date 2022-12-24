package com.jacknie.examples.acl.jpa.acl.oid;

import com.jacknie.examples.acl.jpa.acl.domain.AclDomain;
import com.jacknie.examples.acl.jpa.acl.sid.AclSid;
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
@Table(name = "acl_oid", uniqueConstraints = @UniqueConstraint(columnNames = { "domainId", "identifier" }))
public class AclOid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "domainId", referencedColumnName = "id", nullable = false)
    private AclDomain domain;

    @ManyToOne
    @JoinColumn(name = "ownerSid", referencedColumnName = "id", nullable = false)
    private AclSid ownerSid;

    @ManyToOne
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private AclOid parent;

    @Column(nullable = false)
    private String identifier;

    @Enumerated(EnumType.STRING)
    private AclOidEntriesInheritingType entriesInheritingType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AclOid aclOid = (AclOid) o;
        return id != null && Objects.equals(id, aclOid.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

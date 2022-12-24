package com.jacknie.examples.acl.jpa.acl.entry;

import com.jacknie.examples.acl.jpa.acl.oid.AclOid;
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
@Table(name = "acl_entry", uniqueConstraints = @UniqueConstraint(columnNames = { "oid", "aceOrder" }))
public class AclEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "oid", referencedColumnName = "id", nullable = false)
    private AclOid oid;

    @ManyToOne
    @JoinColumn(name = "sid", referencedColumnName = "id", nullable = false)
    private AclSid sid;

    @Column(name = "aceOrder", nullable = false)
    private Integer order;

    @Column(nullable = false)
    private Integer mask;

    @Column(nullable = false)
    private Boolean granting;

    @Column(nullable = false)
    private Boolean auditSuccess;

    @Column(nullable = false)
    private Boolean auditFailure;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AclEntry aclEntry = (AclEntry) o;
        return id != null && Objects.equals(id, aclEntry.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

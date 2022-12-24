package com.jacknie.examples.acl.jpa.acl.sid;

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
@Table(name = "acl_sid", uniqueConstraints = @UniqueConstraint(columnNames = { "sidType", "sidValue" }))
public class AclSid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "sidType", nullable = false)),
        @AttributeOverride(name = "value", column = @Column(name = "sidValue", nullable = false)),
    })
    private AclSidKey key;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AclSid aclSid = (AclSid) o;
        return id != null && Objects.equals(id, aclSid.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

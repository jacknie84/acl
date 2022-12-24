package com.jacknie.examples.acl.jpa.acl.domain;

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
@Table(name = "acl_domain")
public class AclDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domainCode", nullable = false, unique = true)
    private String code;

    private String idConverter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AclDomain aclDomain = (AclDomain) o;
        return id != null && Objects.equals(id, aclDomain.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

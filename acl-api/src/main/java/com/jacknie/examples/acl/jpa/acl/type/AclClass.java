package com.jacknie.examples.acl.jpa.acl.type;

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
public class AclClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String className;

    private String classIdType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AclClass aclClass = (AclClass) o;
        return Objects.equals(id, aclClass.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

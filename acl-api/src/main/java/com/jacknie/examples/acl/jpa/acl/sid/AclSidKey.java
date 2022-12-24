package com.jacknie.examples.acl.jpa.acl.sid;

import com.jacknie.examples.acl.config.security.acls.domain.SidModel;
import com.jacknie.examples.acl.config.security.acls.domain.SidType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.acls.model.Sid;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AclSidKey {

    @Enumerated(EnumType.STRING)
    private SidType type;

    private String value;

    public static AclSidKey from(Sid sid) {
        SidModel model = SidModel.from(sid);
        return new AclSidKey(model.getType(), model.getValue());
    }
}

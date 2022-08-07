package com.jacknie.examples.acl.web.me;

import com.jacknie.examples.acl.jpa.member.MemberAccount;
import org.springframework.security.oauth2.jwt.Jwt;

public interface MeService {
    MeDto getMe(Jwt jwt);
}

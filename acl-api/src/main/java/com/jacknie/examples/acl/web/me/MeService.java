package com.jacknie.examples.acl.web.me;

import org.springframework.security.oauth2.jwt.Jwt;

public interface MeService {

    MeDto getMe(Jwt jwt);

    void updateMe(Jwt jwt, PutMeDto me);
}

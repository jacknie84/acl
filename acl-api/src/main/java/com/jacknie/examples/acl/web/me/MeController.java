package com.jacknie.examples.acl.web.me;

import com.jacknie.examples.acl.jpa.member.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final MeService meService;

    @GetMapping
    public ResponseEntity<?> getMe(@AuthenticationPrincipal Jwt jwt) {
        MeDto me = meService.getMe(jwt);
        return ResponseEntity.ok(me);
    }
}

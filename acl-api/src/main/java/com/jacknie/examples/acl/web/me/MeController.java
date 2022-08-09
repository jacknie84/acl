package com.jacknie.examples.acl.web.me;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PutMapping
    public ResponseEntity<?> putMe(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid PutMeDto me) {
        meService.updateMe(jwt, me);
        return ResponseEntity.noContent().build();
    }
}

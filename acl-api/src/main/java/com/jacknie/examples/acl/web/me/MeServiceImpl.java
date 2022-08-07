package com.jacknie.examples.acl.web.me;

import com.jacknie.examples.acl.jpa.member.MemberAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {

    private final MemberAccountRepository accountRepository;

    @Override
    public MeDto getMe(Jwt jwt) {
        String email = jwt.getSubject();
        return accountRepository.findByEmail(email)
            .map(entity -> MeDto.builder()
                .email(entity.getEmail())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build())
            .orElseThrow();
    }
}

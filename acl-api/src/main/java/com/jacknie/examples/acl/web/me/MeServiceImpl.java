package com.jacknie.examples.acl.web.me;

import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.jpa.member.MemberAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {

    private final MemberAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public void updateMePassword(Jwt jwt, PatchMePasswordDto dto) {
        String email = jwt.getSubject();
        accountRepository.findByEmail(email).ifPresent(entity -> updateMePassword(entity, dto));
    }

    private void updateMePassword(MemberAccount entity, PatchMePasswordDto dto) {
        if (passwordEncoder.matches(dto.getOrgPassword(), entity.getPassword())) {
            String newPassword = passwordEncoder.encode(dto.getNewPassword());
            entity.setPassword(newPassword);
            accountRepository.save(entity);
        } else {
            throw new AccessDeniedException("wrong password");
        }
    }
}

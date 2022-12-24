package com.jacknie.examples.acl.web.member;

import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.jpa.member.MemberAccountErrorCode;
import com.jacknie.examples.acl.jpa.member.MemberAccountRepository;
import com.jacknie.examples.acl.web.ErrorDto;
import com.jacknie.examples.acl.web.HttpEntityException;
import com.jacknie.examples.acl.web.HttpStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberAccountServiceImpl implements MemberAccountService {

    private final MemberAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public Page<MemberAccountDto> getMemberAccountsPage(MemberAccountsFilterDto dto, Pageable pageable) {
        return accountRepository.findAll(dto, pageable).map(MemberAccountDto::from);
    }

    @Transactional
    @Override
    public long createMemberAccount(PostMemberAccountDto dto) {
        String email = dto.getEmail();
        String password = passwordEncoder.encode(dto.getPassword());
        if (accountRepository.existsByEmail(email)) {
            String format = "요청한 이메일(%s)로 이미 가입되어 있습니다. ";
            String message = String.format(format, email);
            ErrorDto error = ErrorDto.builder()
                .code(MemberAccountErrorCode.ALREADY_IN_USE)
                .message(message)
                .build();
            HttpEntity<?> httpEntity = new HttpEntity<>(error);
            throw new HttpEntityException(HttpStatus.CONFLICT, httpEntity, message);
        }
        MemberAccount entity = accountRepository.save(MemberAccount.builder()
            .email(email)
            .password(password)
            .roles(dto.getRoles())
            .build());
        return entity.getId();
    }

    @Transactional
    @Override
    public void updateMemberAccount(long id, PutMemberAccountDto dto) {
        MemberAccount entity = accountRepository.findById(id)
            .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND));
        Optional.ofNullable(dto.getPassword())
            .filter(StringUtils::hasText)
            .map(passwordEncoder::encode)
            .ifPresent(entity::setPassword);
        entity.getRoles().clear();
        entity.getRoles().addAll(dto.getRoles());
        accountRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<MemberAccountDto> findMemberAccount(long id) {
        return accountRepository.findById(id).map(MemberAccountDto::from);
    }

    @Transactional
    @Override
    public void deleteMemberAccount(long id) {
        MemberAccount entity = accountRepository.findById(id)
            .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND));
        accountRepository.delete(entity);
    }
}

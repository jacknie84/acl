package com.jacknie.examples.acl.web.member;

import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.jpa.member.MemberAccountErrorCode;
import com.jacknie.examples.acl.jpa.member.MemberAccountRepository;
import com.jacknie.examples.acl.web.ErrorDto;
import com.jacknie.examples.acl.web.HttpEntityException;
import com.jacknie.examples.acl.web.HttpStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberAccountServiceImpl implements MemberAccountService {

    private final MemberAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<MemberAccount> getMemberAccountsPage(MemberAccountsFilterDto dto, Pageable pageable) {
        return accountRepository.findAll(dto, pageable);
    }

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
        MemberAccount entity = MemberAccount.builder()
            .email(email)
            .password(password)
            .roles(dto.getRoles())
            .build();
        return accountRepository.save(entity).getId();
    }

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

    @Override
    public Optional<MemberAccountDto> findMemberAccount(long id) {
        return accountRepository.findById(id).map(MemberAccountDto::from);
    }

    @Override
    public void deleteMemberAccount(long id) {
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, e);
        }
    }
}

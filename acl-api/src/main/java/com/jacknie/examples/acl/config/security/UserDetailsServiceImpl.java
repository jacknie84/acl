package com.jacknie.examples.acl.config.security;

import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.jpa.member.MemberAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberAccount account = accountRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("cannot found by email: " + username));
        return User.builder()
            .username(account.getEmail())
            .password(account.getPassword())
            .authorities("ROLE_MEMBER")
            .build();
    }
}

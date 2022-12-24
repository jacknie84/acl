package com.jacknie.examples.acl.web.member.account;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/member/accounts")
@RequiredArgsConstructor
public class MemberAccountController {

    private final MemberAccountService accountService;

    @GetMapping
    public ResponseEntity<?> getMemberAccountsPage(@Valid MemberAccountsFilterDto dto, Pageable pageable) {
        Page<MemberAccountDto> page = accountService.getMemberAccountsPage(dto, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<?> postMemberAccount(@RequestBody @Valid PostMemberAccountDto dto, UriComponentsBuilder uri) {
        long id = accountService.createMemberAccount(dto);
        URI location = uri.path("/member/accounts/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberAccount(@PathVariable("id") long id) {
        return ResponseEntity.of(accountService.findMemberAccount(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putMemberAccount(@PathVariable("id") long id, @RequestBody @Valid PutMemberAccountDto dto) {
        accountService.updateMemberAccount(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMemberAccount(@PathVariable("id") long id) {
        accountService.deleteMemberAccount(id);
        return ResponseEntity.noContent().build();
    }
}

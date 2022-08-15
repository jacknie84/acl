package com.jacknie.examples.acl.web.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/acls")
@RequiredArgsConstructor
public class AclController {

    private final AclService aclService;

    @GetMapping
    public ResponseEntity<?> getAclsPage(@Valid AclFilterDto dto, Pageable pageable) {
        Page<AclDto> page = aclService.getAclsPage(dto, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/entries")
    public ResponseEntity<?> getAclEntries(@PathVariable("id") long id) {
        List<AclEntryDto> entries = aclService.getAclEntries(id);
        return ResponseEntity.ok(entries);
    }
}

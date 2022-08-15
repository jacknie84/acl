package com.jacknie.examples.acl.web.acl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AclService {
    Page<AclDto> getAclsPage(AclFilterDto dto, Pageable pageable);

    List<AclEntryDto> getAclEntries(long id);
}

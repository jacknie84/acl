package com.jacknie.examples.acl.web.acl;

import com.jacknie.examples.acl.jpa.acl.entry.AclEntry;
import com.jacknie.examples.acl.jpa.acl.entry.AclEntryRepository;
import com.jacknie.examples.acl.jpa.acl.oid.AclObjectIdentity;
import com.jacknie.examples.acl.jpa.acl.oid.AclObjectIdentityRepository;
import com.jacknie.examples.acl.jpa.acl.sid.AclSid;
import com.jacknie.examples.acl.web.HttpStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AclServiceImpl implements AclService {

    private final AclObjectIdentityRepository oidRepository;
    private final AclEntryRepository entryRepository;

    @Override
    public Page<AclDto> getAclsPage(AclFilterDto dto, Pageable pageable) {
        Page<AclObjectIdentity> page = oidRepository.findAll(dto, pageable);
        return page.map(entity -> AclDto.builder()
            .id(entity.getId())
            .entriesInheriting(entity.getEntriesInheriting())
            .domainCode(entity.getObjectIdClass().getClassName())
            .owner(toAclSidDto(entity.getOwnerSid()))
            .parentId(Optional.ofNullable(entity.getParentObject()).map(AclObjectIdentity::getId).orElse(null))
            .build());
    }

    @Override
    public List<AclEntryDto> getAclEntries(long id) {
        if (!oidRepository.existsById(id)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND);
        }
        List<AclEntry> entities = entryRepository.findAllByObjectIdentityId(id);
        return entities.stream()
            .map(entity -> AclEntryDto.builder()
                .id(entity.getId())
                .granting(entity.getGranting())
                .read((entity.getMask() & BasePermission.READ.getMask()) != 0)
                .write((entity.getMask() & BasePermission.WRITE.getMask()) != 0)
                .remove((entity.getMask() & BasePermission.DELETE.getMask()) != 0)
                .create((entity.getMask() & BasePermission.CREATE.getMask()) != 0)
                .admin((entity.getMask() & BasePermission.ADMINISTRATION.getMask()) != 0)
                .aclId(entity.getObjectIdentity().getId())
                .sid(toAclSidDto(entity.getSid()))
                .build())
            .toList();
    }

    private AclSidDto toAclSidDto(AclSid entity) {
        return AclSidDto.builder()
            .type(entity.getType())
            .sid(entity.getSid())
            .build();
    }
}

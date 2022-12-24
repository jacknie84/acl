package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CustomAcl implements Acl, MutableAcl, AuditableAcl, OwnershipAcl, AclModelFactory {

    private final long id;
    private final ObjectIdentity oid;
    private final List<CustomAce> entries = new ArrayList<>();
    private final AclAuthorizationStrategy authorizationStrategy;
    private final PermissionGrantingStrategy grantingStrategy;

    private Sid owner;

    @Nullable
    private Acl parentAcl;

    private boolean entriesInheriting;

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public ObjectIdentity getObjectIdentity() {
        return oid;
    }

    @Nullable
    @Override
    public Acl getParentAcl() {
        return parentAcl;
    }

    @Override
    public Sid getOwner() {
        return owner;
    }

    @Override
    public boolean isEntriesInheriting() {
        return entriesInheriting;
    }

    @Override
    public List<AccessControlEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    @Override
    public void setParent(Acl newParent) {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_GENERAL);
        Assert.isTrue(newParent == null || !newParent.equals(this), "Cannot be the parent of yourself");
        this.parentAcl = newParent;
    }

    @Override
    public void setOwner(Sid newOwner) {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_OWNERSHIP);
        Assert.notNull(newOwner, "Owner required");
        this.owner = newOwner;
    }

    @Override
    public void setEntriesInheriting(boolean entriesInheriting) {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_GENERAL);
        this.entriesInheriting = entriesInheriting;
    }

    @Override
    public void insertAce(int atIndexLocation, Permission permission, Sid sid, boolean granting) throws NotFoundException {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_GENERAL);
        Assert.notNull(permission, "permission cannot be null");
        Assert.notNull(sid, "sid cannot be null");
        if (atIndexLocation < 0) {
            throw new NotFoundException("atIndexLocation must be greater than or equal to zero");
        }
        if (atIndexLocation > entries.size()) {
            throw new NotFoundException("atIndexLocation must be less than or equal to the size of the AccessControlEntry collection");
        }
        CustomAce ace = CustomAce.builder()
            .acl(this)
            .permission(permission)
            .sid(sid)
            .granting(granting)
            .build();
        synchronized (entries) {
            entries.add(atIndexLocation, ace);
        }
    }

    @Override
    public void updateAce(int aceIndex, Permission permission) throws NotFoundException {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_GENERAL);
        verifyAceIndexExists(aceIndex);
        synchronized (entries) {
            CustomAce ace = entries.get(aceIndex);
            ace.setPermission(permission);
        }
    }

    @Override
    public void updateAuditing(int aceIndex, boolean auditSuccess, boolean auditFailure) {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_AUDITING);
        verifyAceIndexExists(aceIndex);
        synchronized (entries) {
            CustomAce ace = entries.get(aceIndex);
            ace.setAuditSuccess(auditSuccess);
            ace.setAuditFailure(auditFailure);
        }
    }

    @Override
    public void deleteAce(int aceIndex) throws NotFoundException {
        authorizationStrategy.securityCheck(this, AclAuthorizationStrategy.CHANGE_GENERAL);
        verifyAceIndexExists(aceIndex);
        synchronized (entries) {
            entries.remove(aceIndex);
        }
    }

    @Override
    public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException, UnloadedSidException {
        Assert.notEmpty(permission, "permission cannot be empty");
        Assert.notEmpty(sids, "sids cannot be empty");
        if (!isSidLoaded(sids)) {
            throw new UnloadedSidException("ACL was not loaded for one or more SID");
        }
        return grantingStrategy.isGranted(this, permission, sids, administrativeMode);
    }

    @Override
    public boolean isSidLoaded(List<Sid> sids) {
        // 항상 로드 된 것으로 간주
        return true;
    }

    @Override
    public AclModel createModel() {
        return AclModel.builder()
            .id(id)
            .parentId(getParentId())
            .owner(SidModel.from(owner))
            .oid(OidModel.from(oid))
            .entriesInheriting(entriesInheriting)
            .entries(entries.stream().map(AceModelFactory::createModel).toList())
            .build();
    }

    public void replaceAccessControlEntries(@Nullable List<CustomAce> entries) {
        this.entries.clear();
        if (!CollectionUtils.isEmpty(entries)) {
            this.entries.addAll(entries);
        }
    }

    @Nullable
    private Long getParentId() {
        if (parentAcl != null) {
            Assert.state(parentAcl instanceof CustomAcl, "parentAcl must be an instance of CustomAcl");
            return ((CustomAcl) parentAcl).id;
        } else {
            return null;
        }
    }

    private void verifyAceIndexExists(int aceIndex) {
        if (aceIndex < 0) {
            throw new NotFoundException("aceIndex must be greater than or equal to zero");
        }
        if (aceIndex >= entries.size()) {
            throw new NotFoundException("aceIndex must refer to an index of the AccessControlEntry list. "
                + "List size is " + entries.size() + ", index was " + aceIndex);
        }
    }
}

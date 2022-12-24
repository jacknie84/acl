package com.jacknie.examples.acl.config.security.acls.cache;

import com.jacknie.examples.acl.config.security.acls.domain.CustomAcl;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

import java.io.Serializable;

public abstract class AbstractAclCache implements AclCache {

    @Override
    public void evictFromCache(Serializable pk) {
        Assert.notNull(pk, "pk cannot be null");
        MutableAcl acl = getFromCache(pk);
        if (acl != null) {
            evictFromCacheInternal(acl);
        }
    }

    @Override
    public void evictFromCache(ObjectIdentity oid) {
        Assert.notNull(oid, "oid cannot be null");
        MutableAcl acl = getFromCache(oid);
        if (acl != null) {
            evictFromCacheInternal(acl);
        }
    }

    @Nullable
    @Override
    public MutableAcl getFromCache(ObjectIdentity oid) {
        Assert.notNull(oid, "oid cannot be null");
        return getFromCacheInternal(oid);
    }

    @Nullable
    @Override
    public MutableAcl getFromCache(Serializable pk) {
        Assert.notNull(pk, "pk cannot be null");
        return getFromCacheInternal(pk);
    }

    @SneakyThrows
    @Override
    public void putInCache(MutableAcl acl) {
        Assert.notNull(acl, "acl cannot be null");
        Assert.notNull(acl.getObjectIdentity(), "acl.objectIdentity cannot be null");
        Assert.notNull(acl.getId(), "acl.id cannot be null");
        Assert.isInstanceOf(CustomAcl.class, acl);
        if ((acl.getParentAcl() != null) && (acl.getParentAcl() instanceof MutableAcl)) {
            putInCache((MutableAcl) acl.getParentAcl());
        }
        putInCacheInternal(acl);
    }

    protected abstract void evictFromCacheInternal(MutableAcl acl);

    protected abstract void putInCacheInternal(MutableAcl acl);

    protected abstract MutableAcl getFromCacheInternal(Object key);
}

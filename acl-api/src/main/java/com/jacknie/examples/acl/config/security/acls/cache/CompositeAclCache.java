package com.jacknie.examples.acl.config.security.acls.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

import java.io.Serializable;

@RequiredArgsConstructor
public class CompositeAclCache implements AclCache, AclCacheEvictionEventListener {

    private final AclCache memoryCache;
    private final AclCache redisCache;

    @Override
    public void evictFromCache(Serializable pk) {
        memoryCache.evictFromCache(pk);
        redisCache.evictFromCache(pk);
    }

    @Override
    public void evictFromCache(ObjectIdentity oid) {
        memoryCache.evictFromCache(oid);
        redisCache.evictFromCache(oid);
    }

    @Override
    public MutableAcl getFromCache(ObjectIdentity oid) {
        MutableAcl acl = memoryCache.getFromCache(oid);
        if (acl == null) {
            acl = redisCache.getFromCache(oid);
            memoryCache.putInCache(acl);
        }
        return acl;
    }

    @Override
    public MutableAcl getFromCache(Serializable pk) {
        MutableAcl acl = memoryCache.getFromCache(pk);
        if (acl == null) {
            acl = redisCache.getFromCache(pk);
            memoryCache.putInCache(acl);
        }
        return acl;
    }

    @Override
    public void putInCache(MutableAcl acl) {
        memoryCache.putInCache(acl);
        redisCache.putInCache(acl);
    }

    @Override
    public void clearCache() {
        memoryCache.clearCache();
        redisCache.clearCache();
    }

    @Override
    public void onEvict(Serializable pk, ObjectIdentity oid) {
        Assert.notNull(pk, "pk cannot be null");
        Assert.notNull(oid, "oid cannot be null");
        MutableAcl acl = memoryCache.getFromCache(pk);
        if (acl == null) {
            acl = memoryCache.getFromCache(oid);
        }
        if (acl != null) {
            memoryCache.evictFromCache(acl.getId());
            memoryCache.evictFromCache(acl.getObjectIdentity());
        }
    }
}

package com.jacknie.examples.acl.config.security.acls.cache;

import com.jacknie.examples.acl.config.security.acls.domain.CustomAcl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.security.acls.model.MutableAcl;

@RequiredArgsConstructor
public class MemoryAclCache extends AbstractAclCache {

    private final Cache cache;

    @Override
    protected void evictFromCacheInternal(MutableAcl acl) {
        cache.evict(acl.getId());
        cache.evict(acl.getObjectIdentity());
    }

    @Override
    protected void putInCacheInternal(MutableAcl acl) {
        CustomAcl customAcl = (CustomAcl) acl;
        cache.put(acl.getObjectIdentity(), customAcl);
        cache.put(acl.getId(), customAcl);
    }

    @Override
    protected MutableAcl getFromCacheInternal(Object key) {
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return (MutableAcl) valueWrapper.get();
    }

    @Override
    public void clearCache() {
        cache.clear();
    }
}

package io.skysail.server.caches;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

import io.skysail.server.restlet.response.messages.Message;
import lombok.Getter;

public class Caches {
    
    private Caches() {
    }

    @Getter
    private static Cache<Long, Message> messageCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .build();
    
    public static CacheStats getMessageCacheStats() {
        return messageCache.stats();
    }
}

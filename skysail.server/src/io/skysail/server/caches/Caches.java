package io.skysail.server.caches;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.*;

import io.skysail.server.restlet.response.messages.Message;
import lombok.Getter;

public class Caches {

    @Getter
    private static Cache<Long, Message> messageCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();
}

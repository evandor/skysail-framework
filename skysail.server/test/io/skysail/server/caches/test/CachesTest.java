package io.skysail.server.caches.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.server.caches.Caches;
import io.skysail.server.restlet.response.messages.Message;
import io.skysail.server.restlet.response.messages.MessageType;

public class CachesTest {

    @Test
    public void cache_is_active() {
        Caches.getMessageCache().put(17L, new Message(MessageType.INFO, "info"));
        Message message = Caches.getMessageCache().getIfPresent(17L);
        assertThat(message.getType(), is(MessageType.INFO));
        assertThat(message.getMsg(), is("info"));
        assertThat(Caches.getMessageCacheStats(),is(notNullValue()));
    }
}

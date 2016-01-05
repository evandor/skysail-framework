package io.skysail.server.restlet.response;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.caches.Caches;
import io.skysail.server.restlet.response.messages.*;
import lombok.*;

public abstract class AbstractResponseWrapper<T extends Identifiable> implements Wrapper<T> {

    @Getter
    @Setter
    protected Response response;

    protected ConstraintViolationsResponse<T> constraintViolationsResponse;
    
    private static AtomicLong cacheId = new AtomicLong();
    
    @Getter
    private List<Long> messageIds = new ArrayList<>();
    
   

    @Override
    public synchronized void addError(String msg) {
        Caches.getMessageCache().put(getAndRememberNewMessageId(), new Message(MessageType.ERROR, msg));
    }

    @Override
    public void addInfo(String msg) {
        Caches.getMessageCache().put(getAndRememberNewMessageId(), new Message(MessageType.INFO, msg));
    }

    @Override
    public void addWarning(String msg) {
        Caches.getMessageCache().put(getAndRememberNewMessageId(), new Message(MessageType.WARNING, msg));
    }

    private long getAndRememberNewMessageId() {
        long messageId = cacheId.getAndIncrement();
        messageIds.add(messageId);
        return messageId;
    }

}

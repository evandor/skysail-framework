package io.skysail.server.restlet.response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.caches.Caches;
import io.skysail.server.restlet.response.messages.Message;
import io.skysail.server.restlet.response.messages.MessageType;
import lombok.Getter;
import lombok.Setter;

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
        response.getRequest().getAttributes().put("message.error", msg);
        Caches.getMessageCache().put(getAndRememberNewMessageId(), new Message(MessageType.ERROR, msg));
    }

    @Override
    public void addInfo(String msg) {
        response.getRequest().getAttributes().put("message.info", msg);
        Caches.getMessageCache().put(getAndRememberNewMessageId(), new Message(MessageType.INFO, msg));
    }

    @Override
    public void addWarning(String msg) {
        response.getRequest().getAttributes().put("message.warning", msg);
        Caches.getMessageCache().put(getAndRememberNewMessageId(), new Message(MessageType.WARNING, msg));
    }

    private long getAndRememberNewMessageId() {
        long messageId = cacheId.getAndIncrement();
        messageIds.add(messageId);
        return messageId;
    }

}

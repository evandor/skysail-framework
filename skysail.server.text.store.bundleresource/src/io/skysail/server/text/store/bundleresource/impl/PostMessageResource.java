package io.skysail.server.text.store.bundleresource.impl;

import org.restlet.resource.ResourceException;

import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostMessageResource extends PostEntityServerResource<Message> {

    private I18nApplication app;
    private String msgKey;

    public PostMessageResource() {
        app = (I18nApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        msgKey = getAttribute("key");
    }
    
    @Override
    public Message createEntityTemplate() {
        return new Message("");
    }

    
    @Override
    public void addEntity(Message entity) {
        if (!entity.getMsgKey().equals(msgKey)) {
            throw new IllegalStateException("wrong key");
        }
        app.setMessage(entity);
    }

    // for stringtemplate
    public boolean isMessageResource() {
        return true;
    }

//    @Override
//    public String redirectBackTo() {
//        Reference referrerRef = getRequest().getReferrerRef();
//        return referrerRef != null ? referrerRef.toString() : "/";
//    }

//    @Override
//    public Message getEntity() {
//        Message message = app.getMessage(msgKey, store, this);
//        if (message.getPreferredRenderer() != null) {
//            String rendererHint = message.getPreferredRenderer().getClass().getSimpleName();
//            getContext().getAttributes().put(ResourceContextId.RENDERER_HINT.name(), rendererHint);
//        }
//        return message;
//    }


}

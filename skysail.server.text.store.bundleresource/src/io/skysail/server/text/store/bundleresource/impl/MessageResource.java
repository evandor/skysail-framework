package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class MessageResource extends EntityServerResource<Message> {

    private I18nApplication app;
    private String msgKey;
    private String store;

    public MessageResource() {
        app = (I18nApplication) getApplication();
    }

    protected void doInit() throws ResourceException {
        msgKey = getAttribute("key");
        store = getQueryValue("store");    }

    public boolean isMessageResource() {
        return true;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Message getEntity() {
        Message message = app.getMessage(msgKey, store, this);
        if (message.getPreferredRenderer() != null) {
            String rendererHint = message.getPreferredRenderer().getClass().getSimpleName();
            getContext().getAttributes().put(ResourceContextId.RENDERER_HINT.name(), rendererHint);
        }
        return message;
    }
}

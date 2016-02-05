package io.skysail.server.text.store.bundleresource.impl;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PatchEntityServerResource;

public class PatchMessageResource extends PatchEntityServerResource<Message> {

    private I18nApplication app;

    public PatchMessageResource() {
        app = (I18nApplication) getApplication();
    }

    public void updateEntity(Message entity) {
        if (!entity.getMsgKey().equals(getAttribute("key"))) {
            throw new IllegalStateException("wrong key");
        }
        app.setMessage(entity);
    }

    @Override
    public Message getEntity() {
        Message message = app.getMessage(getAttribute("key"), getQueryValue("store"), this);
        if (message.getPreferredRenderer() != null) {
            String rendererHint = message.getPreferredRenderer().getClass().getSimpleName();
            getContext().getAttributes().put(ResourceContextId.RENDERER_HINT.name(), rendererHint);
        }
        return message;
    }
   
}

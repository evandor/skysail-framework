package io.skysail.server.text.store.bundleresource.impl;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class MessageResource extends EntityServerResource<Message> {

    private I18nApplication app;
    private String msgKey;

    public MessageResource() {
        app = (I18nApplication) getApplication();
    }

    protected void doInit() throws ResourceException {
        msgKey = getAttribute("key");
    }

    @Override
    public Message getData() {
        return app.getMessage(msgKey);
    }

    // for stringtemplate
    public boolean isMessageResource() {
        return true;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }
}

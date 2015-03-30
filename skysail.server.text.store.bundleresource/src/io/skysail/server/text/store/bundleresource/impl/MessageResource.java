package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.responses.SkysailResponse;

import org.restlet.resource.ResourceException;

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


    // for stringtemplate
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
        return null;
    }
}

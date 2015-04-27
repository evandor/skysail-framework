package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.restlet.resource.ResourceException;

public class MessageResource extends EntityServerResource<Message> {

    private SkysailApplication app;
    private String msgKey;

    public MessageResource() {
        app = (SkysailApplication) getApplication();
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

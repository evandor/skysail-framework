package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

public class PutMessageResource extends PutEntityServerResource<Message> {

    private I18nApplication app;
    private String msgKey;

    public PutMessageResource() {
        app = (I18nApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        msgKey = getAttribute("key");
    }

    // @Override
    public Message getData2(Form form) {
        return new Message(msgKey, form.getFirstValue("msg"));
    }

    @Override
    public SkysailResponse<?> updateEntity(Message entity) {
        if (!entity.getMsgKey().equals(msgKey)) {
            throw new IllegalStateException("wrong key");
        }
        app.setMessage(entity);
        return new SkysailResponse<String>();
    }

    // for stringtemplate
    public boolean isMessageResource() {
        return true;
    }

    @Override
    public String redirectBackTo() {
        Reference referrerRef = getRequest().getReferrerRef();
        return referrerRef != null ? referrerRef.toString() : "/";
    }

    @Override
    public Message getEntity() {
        return null;
    }

}

package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.responses.SkysailResponse;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

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

    @Override
    public JSONObject getEntityAsJsonObject() {
        return null;// app.getMessage(msgKey);
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

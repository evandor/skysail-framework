package io.skysail.server.ext.apt.test.crm.contacts;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutContactResource extends PutEntityServerResource<Contact> {

    private String id;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public JSONObject getEntity() {
        return null;// ContactsRepository.getInstance().getById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Contact entity) {
        ContactsRepository.getInstance().update(entity);
        return null;
    }

}

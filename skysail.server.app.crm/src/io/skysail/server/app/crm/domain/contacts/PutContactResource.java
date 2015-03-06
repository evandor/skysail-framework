package io.skysail.server.app.crm.domain.contacts;

import io.skysail.server.app.crm.domain.CrmRepository;

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
    public Contact getEntity() {
        return (Contact) CrmRepository.getInstance().getById(Contact.class, id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Contact entity) {
        // CompaniesRepository.getInstance().update(entity);
        return null;
    }

    @Override
    public SkysailResponse<?> updateEntity(JSONObject json) {
        // CrmRepository.getInstance().update(json);
        return new SkysailResponse<String>();
    }

}

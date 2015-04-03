package io.skysail.server.app.crm.contacts;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.crm.CrmApplication;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutContactResource extends PutEntityServerResource<Contact> {

    private String id;
    private CrmApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (CrmApplication) getApplication();
    }

    @Override
    public Contact getEntity() {
        return app.getRepository().getById(Contact.class, id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Contact entity) {
        // CompaniesRepository.getInstance().update(entity);
        return null;
    }

}

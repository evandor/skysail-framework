package io.skysail.server.app.crm.domain.companies;

import io.skysail.server.app.crm.ContactsGen;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutCompanyResource extends PutEntityServerResource<Company> {

    private String id;
    private ContactsGen app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ContactsGen) getApplication();
    }

    @Override
    public Company getEntity() {
        return app.getRepository().getById(Company.class, id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Company entity) {
        // CompaniesRepository.getInstance().update(entity);
        return null;
    }

    @Override
    public SkysailResponse<?> updateEntity(JSONObject json) {
        // CrmRepository.getInstance().update(json);
        return new SkysailResponse<String>();
    }

}

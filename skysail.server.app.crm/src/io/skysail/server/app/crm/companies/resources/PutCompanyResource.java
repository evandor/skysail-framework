package io.skysail.server.app.crm.companies.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.crm.CrmApplication;
import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

public class PutCompanyResource extends PutEntityServerResource<Company> {

    private String id;
    private CrmApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (CrmApplication) getApplication();
    }

    @Override
    public Company getEntity() {
        return app.getRepository().getById(Company.class, id);
    }

    @Override
    public SkysailResponse<Company> updateEntity(Company entity) {
        // entity.setChanged(new Date());
        entity.setChangedBy(SecurityUtils.getSubject().getPrincipal().toString());
        app.getRepository().update(id, entity);
        return new SkysailResponse<>();
    }

}

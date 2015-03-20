package io.skysail.server.app.crm.companies.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.crm.CrmApplication;
import io.skysail.server.app.crm.companies.Company;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class CompanyResource extends EntityServerResource<Company> {

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
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // CompaniesRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PutCompanyResource.class);
    }

}

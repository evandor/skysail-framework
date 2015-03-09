package io.skysail.server.app.crm.companies;

import io.skysail.server.app.crm.ContactsGen;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class CompanyResource extends EntityServerResource<Company> {

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
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // CompaniesRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutCompanyResource.class);
    }

}

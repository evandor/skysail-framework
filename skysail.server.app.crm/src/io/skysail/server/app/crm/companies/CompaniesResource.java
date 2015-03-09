package io.skysail.server.app.crm.companies;

import io.skysail.server.app.crm.ContactsGen;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class CompaniesResource extends ListServerResource<Company> {

    private ContactsGen app;

    public CompaniesResource() {
        super(CompanyResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Companies");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ContactsGen) getApplication();
    }

    @Override
    public List<Company> getEntity() {
        return app.getRepository().findAll(Company.class);
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostCompanyResource.class);
    }

}

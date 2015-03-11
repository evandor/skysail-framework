package io.skysail.server.app.crm.companies;

import io.skysail.server.app.crm.CrmApplication;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class CompaniesResource extends ListServerResource<Company> {

    private CrmApplication app;

    public CompaniesResource() {
        super(CompanyResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Companies");
    }

    @Override
    protected void doInit() {
        app = (CrmApplication) getApplication();
    }

    @Override
    public List<Company> getEntity() {
        List<Company> companies = app.getRepository().findAll(Company.class);
        /*
         * for (Company company : companies) { Object id = company.getId();
         * app.getRepository().getById(Company.class, id); }
         */
        return companies;
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostCompanyResource.class);
    }

}

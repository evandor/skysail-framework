package io.skysail.server.app.crm.domain.companies;

import io.skysail.server.app.crm.domain.CrmRepository;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class CompaniesResource extends ListServerResource<Company> {

    public CompaniesResource() {
        super(CompanyResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Companies");
    }

    @Override
    public List<Company> getEntity() {
        return CrmRepository.getInstance().findAll(Company.class);
    }

    // @Override
    // protected List<String> getDataAsJson() {
    // String username = SecurityUtils.getSubject().getPrincipal().toString();
    // return CrmRepository.getInstance().getCompanysAsJson(username);
    // }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostCompanyResource.class);
    }

}

package io.skysail.server.app.crm.companies.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.crm.CrmRepository;
import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostCompanyResource extends PostEntityServerResource<Company> {

    public PostCompanyResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Company");
    }

    @Override
    public Company createEntityTemplate() {
        String creator = SecurityUtils.getSubject().getPrincipal().toString();
        return new Company(creator);
    }

    @Override
    public SkysailResponse<?> addEntity(Company entity) {
        CrmRepository.add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CompaniesResource.class);
    }
}
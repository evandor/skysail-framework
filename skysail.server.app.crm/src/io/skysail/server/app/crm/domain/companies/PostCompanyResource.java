package io.skysail.server.app.crm.domain.companies;

import io.skysail.server.app.crm.domain.CrmRepository;

import org.apache.shiro.SecurityUtils;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
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
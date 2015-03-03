package io.skysail.server.app.crm.domain.companies;

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
        return new Company();
    }

    @Override
    public SkysailResponse<?> addEntity(Company entity) {
        entity.setOwner(SecurityUtils.getSubject().getPrincipal().toString());
        entity = CompaniesRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CompaniesResource.class);
    }
}
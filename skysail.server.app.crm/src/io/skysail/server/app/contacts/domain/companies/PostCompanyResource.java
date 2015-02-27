package io.skysail.server.app.contacts.domain.companies;

import io.skysail.server.app.contacts.ContactsGen;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostCompanyResource extends PostEntityServerResource<Company> {

    private ContactsGen app;

    public PostCompanyResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Company");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ContactsGen) getApplication();
    }

    @Override
    public Company createEntityTemplate() {
        return new Company();
    }

    @Override
    public SkysailResponse<?> addEntity(Company entity) {
        entity = CompanysRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CompanysResource.class);
    }
}
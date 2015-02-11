package io.skysail.server.ext.apt.test.withlist.companies;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.withlist.companies.*;


import de.twenty11.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.apt.test.withlist.CompaniesGen;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ServerLink;

public class PostCompanyResource extends PostEntityServerResource<Company> {

    private String id;

	private CompaniesGen app;

	public PostCompanyResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Company");
    }

    @Override
	protected void doInit() throws ResourceException {
		app = (CompaniesGen)getApplication();
		id = getAttribute("id");
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
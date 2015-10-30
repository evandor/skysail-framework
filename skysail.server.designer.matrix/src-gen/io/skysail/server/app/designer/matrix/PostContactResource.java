package io.skysail.server.app.designer.matrix;

import javax.annotation.Generated;

//import io.skysail.server.app.designer.matrix.*;


import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostContactResource extends PostEntityServerResource<Contact> {

	private io.skysail.server.app.designer.matrix.MatrixApplication app;
    private ContactRepo repository;

	public PostContactResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Contact");
    }

    @Override
	protected void doInit() {
		app = (io.skysail.server.app.designer.matrix.MatrixApplication)getApplication();
        repository = (ContactRepo) app.getRepository(Contact.class);
	}

	@Override
    public Contact createEntityTemplate() {
	    return new Contact();
    }

    @Override
    public SkysailResponse<Contact> addEntity(Contact entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ContactsResource.class);
	}
}
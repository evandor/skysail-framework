package io.skysail.server.ext.apt.test.crm.contacts;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.crm.contacts.*;


import de.twenty11.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.apt.test.crm.CrmGen;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ServerLink;

public class PostContactResource extends PostEntityServerResource<Contact> {

    private String id;

	private CrmGen app;

	public PostContactResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Contact");
    }

    @Override
	protected void doInit() throws ResourceException {
		app = (CrmGen)getApplication();
		id = getAttribute("id");
	}

	@Override
    public Contact createEntityTemplate() {
	    return new Contact();
    }

	@Override
    public SkysailResponse<?> addEntity(Contact entity) {
		entity = ContactsRepository.getInstance().add(entity);
	    return new SkysailResponse<String>();
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ContactsResource.class);
	}
}
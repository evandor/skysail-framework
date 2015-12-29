package io.skysail.server.app.cRM;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ContactResource extends EntityServerResource<io.skysail.server.app.cRM.Contact> {

    private String id;
    private CRMApplication app;
    private ContactRepository repository;

    public ContactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (CRMApplication) getApplication();
        repository = (ContactRepository) app.getRepository(io.skysail.server.app.cRM.Contact.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.cRM.Contact getEntity() {
        return (io.skysail.server.app.cRM.Contact)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutContactResource.class);
    }

}
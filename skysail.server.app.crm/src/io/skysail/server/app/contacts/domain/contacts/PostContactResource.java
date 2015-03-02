package io.skysail.server.app.contacts.domain.contacts;

import io.skysail.server.app.contacts.ContactsGen;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostContactResource extends PostEntityServerResource<Contact> {

    private String id;

    private ContactsGen app;

    public PostContactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Contact");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ContactsGen) getApplication();
        id = getAttribute("id");
    }

    // @Override
    // public Contact getData(Form form) {
    // Contact entity = populate(createEntityTemplate(), form);
    // entity.setOwner(SecurityUtils.getSubject().getPrincipal().toString());
    // return entity;
    // }

    @Override
    public Contact createEntityTemplate() {
        return new Contact();
    }

    @Override
    public SkysailResponse<?> addEntity(Contact entity) {
        entity.setOwner(SecurityUtils.getSubject().getPrincipal().toString());
        entity = ContactsRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ContactsResource.class);
    }
}
package io.skysail.server.app.crm.contacts;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class ContactResource extends EntityServerResource<Contact> {

    private String id;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    // @Override
    // public Contact getData() {
    // return ContactsRepository.getInstance().getById(id);
    // }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // ContactsRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PutContactResource.class);
    }

    @Override
    public Contact getEntity() {
        return null;
    }

}

package io.skysail.server.app.cRM;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ContactsResource extends ListServerResource<io.skysail.server.app.cRM.Contact> {

    private CRMApplication app;
    private ContactRepository repository;

    public ContactsResource() {
        super(ContactResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Contacts");
    }

    @Override
    protected void doInit() {
        app = (CRMApplication) getApplication();
        repository = (ContactRepository) app.getRepository(io.skysail.server.app.cRM.Contact.class);
    }

    @Override
    public List<io.skysail.server.app.cRM.Contact> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
       return super.getLinks(PostContactResource.class);
    }
}
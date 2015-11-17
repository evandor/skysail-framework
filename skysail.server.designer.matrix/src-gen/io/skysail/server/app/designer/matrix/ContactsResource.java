package io.skysail.server.app.designer.matrix;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ContactsResource extends ListServerResource<Contact> {

    private MatrixApplication app;
    private ContactRepo repository;

    public ContactsResource() {
        super(ContactResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Contacts");
    }

    protected void doInit() {
        super.doInit();
        app = (MatrixApplication)getApplication();
        repository = (ContactRepo) app.getRepository(Contact.class);
    }

    @Override
    public List<Contact> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(io.skysail.server.app.designer.matrix.PostContactResource.class);
    }
}

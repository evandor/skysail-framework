package io.skysail.server.ext.apt.test.crm.contacts;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.crm.contacts.*;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ContactsResource extends ListServerResource<Contact> {

    private String id;

    public ContactsResource() {
        super(ContactResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Contacts");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public List<Contact> getData() {
        return ContactsRepository.getInstance().getContacts();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostContactResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> { l.substitute("id", id); };
    }
}

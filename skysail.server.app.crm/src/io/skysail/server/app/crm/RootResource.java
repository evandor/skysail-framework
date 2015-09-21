package io.skysail.server.app.crm;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.Link;
import io.skysail.server.app.crm.companies.resources.CompaniesResource;
import io.skysail.server.app.crm.contacts.ContactsResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<Identifiable> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(CompaniesResource.class, ContactsResource.class);
    }

    @Override
    public List<Identifiable> getEntity() {
        return null;
    }

}
package io.skysail.server.app.contacts;

import io.skysail.server.app.contacts.domain.companies.CompaniesResource;
import io.skysail.server.app.contacts.domain.contacts.ContactsResource;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<String> {

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(CompaniesResource.class, ContactsResource.class);
    }

}
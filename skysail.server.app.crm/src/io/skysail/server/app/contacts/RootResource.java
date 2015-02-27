package io.skysail.server.app.contacts;

import io.skysail.server.app.contacts.domain.ContactsResource;
import io.skysail.server.app.contacts.domain.companies.CompanysResource;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<String> {

    @Override
    public List<String> getData() {
        return Collections.emptyList();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(CompanysResource.class, ContactsResource.class);
    }

}
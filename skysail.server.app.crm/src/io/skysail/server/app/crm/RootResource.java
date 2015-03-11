package io.skysail.server.app.crm;

import io.skysail.server.app.crm.companies.resources.CompaniesResource;
import io.skysail.server.app.crm.contacts.ContactsResource;
import io.skysail.server.app.crm.contracts.ContractsResource;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<String> {

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(CompaniesResource.class, ContactsResource.class, ContractsResource.class);
    }

    @Override
    public List<String> getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
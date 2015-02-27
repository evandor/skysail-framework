package io.skysail.server.app.contacts.domain.companies;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.contacts.domain.companies.*;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class CompanysResource extends ListServerResource<Company> {

    private String id;

    public CompanysResource() {
        super(CompanyResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Companys");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public List<Company> getData() {
        return CompanysRepository.getInstance().getCompanys();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostCompanyResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> { l.substitute("id", id); };
    }
}

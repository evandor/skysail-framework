package io.skysail.server.app.crm;

import io.skysail.server.app.crm.domain.CrmRepository;
import io.skysail.server.app.crm.domain.companies.CompaniesResource;
import io.skysail.server.app.crm.domain.companies.CompanyResource;
import io.skysail.server.app.crm.domain.companies.PostCompanyResource;
import io.skysail.server.app.crm.domain.companies.PutCompanyResource;
import io.skysail.server.app.crm.domain.contacts.ContactResource;
import io.skysail.server.app.crm.domain.contacts.ContactsResource;
import io.skysail.server.app.crm.domain.contacts.PostContactResource;
import io.skysail.server.app.crm.domain.contacts.PutContactResource;
import io.skysail.server.app.crm.domain.contracts.ContractsResource;
import io.skysail.server.app.crm.domain.contracts.PostContractResource;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class ContactsGen extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    private static final String APP_NAME = "ContactsGen";

    private CrmRepository crmRepo;

    public ContactsGen() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setCrmRepository(DbRepository repo) {
        this.crmRepo = (CrmRepository) repo;
    }

    public void unsetCrmRepository(DbRepository repo) {
        this.crmRepo = null;
    }

    public CrmRepository getRepository() {
        return crmRepo;
    }

    @Override
    protected void attach() {
        // Application root resource
        router.attach(new RouteBuilder("", RootResource.class));

        router.attach(new RouteBuilder("/Contacts/", PostContactResource.class));
        router.attach(new RouteBuilder("/Contacts", ContactsResource.class));
        router.attach(new RouteBuilder("/Contacts/{id}", ContactResource.class));
        router.attach(new RouteBuilder("/Contacts/{id}/", PutContactResource.class));

        router.attach(new RouteBuilder("/Company/", PostCompanyResource.class));
        router.attach(new RouteBuilder("/Companies", CompaniesResource.class));
        router.attach(new RouteBuilder("/Companies/{id}", CompanyResource.class));
        router.attach(new RouteBuilder("/Companies/{id}/", PutCompanyResource.class));

        router.attach(new RouteBuilder("/Contract/", PostContractResource.class));
        router.attach(new RouteBuilder("/Contracts", ContractsResource.class));
        // router.attach(new RouteBuilder("/Contracts/{id}",
        // ContractResource.class));
        // router.attach(new RouteBuilder("/Contracts/{id}/",
        // PutContractResource.class));

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("ContactsGen", "/ContactsGen", this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}
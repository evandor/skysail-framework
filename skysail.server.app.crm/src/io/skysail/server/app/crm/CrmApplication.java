package io.skysail.server.app.crm;

import io.skysail.server.app.crm.companies.CompaniesResource;
import io.skysail.server.app.crm.companies.CompanyResource;
import io.skysail.server.app.crm.companies.PostCompanyResource;
import io.skysail.server.app.crm.companies.PutCompanyResource;
import io.skysail.server.app.crm.contacts.ContactResource;
import io.skysail.server.app.crm.contacts.ContactsResource;
import io.skysail.server.app.crm.contacts.PostContactResource;
import io.skysail.server.app.crm.contacts.PutContactResource;
import io.skysail.server.app.crm.contracts.ContractsResource;
import io.skysail.server.app.crm.contracts.PostContractResource;

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
public class CrmApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    private static final String APP_NAME = "CRM";

    private CrmRepository crmRepo;

    public CrmApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/vcard.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=CrmRepository)")
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

        router.attach(new RouteBuilder("/Contracts/", PostContractResource.class));
        router.attach(new RouteBuilder("/Contracts", ContractsResource.class));
        // router.attach(new RouteBuilder("/Contracts/{id}",
        // ContractResource.class));
        // router.attach(new RouteBuilder("/Contracts/{id}/",
        // PutContractResource.class));

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}
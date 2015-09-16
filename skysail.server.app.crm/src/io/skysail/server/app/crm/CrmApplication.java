package io.skysail.server.app.crm;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.crm.companies.resources.*;
import io.skysail.server.app.crm.contacts.*;
import io.skysail.server.db.DbRepository;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

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
        super.attach();
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

//        router.attach(new RouteBuilder("/Contracts/", PostContractResource.class));
//        router.attach(new RouteBuilder("/Contracts", ContractsResource.class));
        // router.attach(new RouteBuilder("/Contracts/{id}",
        // ContractResource.class));
        // router.attach(new RouteBuilder("/Contracts/{id}/",
        // PutContractResource.class));

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}
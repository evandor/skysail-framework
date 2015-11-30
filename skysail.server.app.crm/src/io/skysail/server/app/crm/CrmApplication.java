package io.skysail.server.app.crm;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.crm.companies.resources.*;
import io.skysail.server.app.crm.contacts.*;
import io.skysail.server.menus.*;
import lombok.Getter;

@Component(immediate = true)
public class CrmApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    private static final String APP_NAME = "CRM";

    private CrmRepository crmRepo;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public CrmApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/vcard.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=CrmRepository)")
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
package io.skysail.server.app.crm;

import io.skysail.server.app.crm.domain.companies.CompaniesResource;
import io.skysail.server.app.crm.domain.companies.CompanyResource;
import io.skysail.server.app.crm.domain.companies.PostCompanyResource;
import io.skysail.server.app.crm.domain.companies.PutCompanyResource;
import io.skysail.server.app.crm.domain.contacts.ContactResource;
import io.skysail.server.app.crm.domain.contacts.ContactsResource;
import io.skysail.server.app.crm.domain.contacts.PostContactResource;
import io.skysail.server.app.crm.domain.contacts.PutContactResource;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateSkysailApplicationProcessor")
public class ContactsGen extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    private static final String APP_NAME = "ContactsGen";

    public ContactsGen() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("ContactsGen", "/ContactsGen", this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}
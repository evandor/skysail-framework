package io.skysail.server.app.cRM;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class CRMApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "CRM";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public CRMApplication() {
        super("CRM", new ApiVersion(1), Arrays.asList());
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/io.skysail.server.app.cRM.Contacts/{id}", io.skysail.server.app.cRM.ContactResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.cRM.Contacts/", io.skysail.server.app.cRM.PostContactResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.cRM.Contacts/{id}/", io.skysail.server.app.cRM.PutContactResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.cRM.Contacts", io.skysail.server.app.cRM.ContactsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.cRM.ContactsResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
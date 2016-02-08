package io.skysail.server.app.argusAdmin;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class ArgusAdminApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "ArgusAdmin";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public ArgusAdminApplication() {
        super("ArgusAdmin", new ApiVersion(1), Arrays.asList());
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
        router.attach(new RouteBuilder("/io.skysail.server.app.argusAdmin.Groups/{id}", io.skysail.server.app.argusAdmin.GroupResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.argusAdmin.Groups/", io.skysail.server.app.argusAdmin.PostGroupResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.argusAdmin.Groups/{id}/", io.skysail.server.app.argusAdmin.PutGroupResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.argusAdmin.Groups", io.skysail.server.app.argusAdmin.GroupsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.argusAdmin.GroupsResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
package io.skysail.server.app.oEService;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class OEServiceApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "OEService";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public OEServiceApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
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
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.OEs/{id}", io.skysail.server.app.oEService.OEResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.OEs/", io.skysail.server.app.oEService.PostOEResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.OEs/{id}/", io.skysail.server.app.oEService.PutOEResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.OEs", io.skysail.server.app.oEService.OEsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.oEService.OEsResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users/{id}", io.skysail.server.app.oEService.UserResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users/", io.skysail.server.app.oEService.PostUserResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users/{id}/", io.skysail.server.app.oEService.PutUserResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users", io.skysail.server.app.oEService.UsersResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.oEService.UsersResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
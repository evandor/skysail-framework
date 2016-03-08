package io.skysail.server.app.oEService;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
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
        router.attach(new RouteBuilder("/OEs/{id}", io.skysail.server.app.oEService.oe.resources.OEResourceGen.class));
        router.attach(new RouteBuilder("/OEs/", io.skysail.server.app.oEService.oe.resources.PostOEResourceGen.class));
        router.attach(new RouteBuilder("/OEs/{id}/", io.skysail.server.app.oEService.oe.resources.PutOEResourceGen.class));
        router.attach(new RouteBuilder("/OEs", io.skysail.server.app.oEService.oe.resources.OEsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.oEService.oe.resources.OEsResourceGen.class));
        router.attach(new RouteBuilder("/OEs/{id}/OEs", io.skysail.server.app.oEService.oe.OEsOEsResource.class));
        router.attach(new RouteBuilder("/OEs/{id}/OEs/", io.skysail.server.app.oEService.oe.PostOEToNewOERelationResource.class));
        router.attach(new RouteBuilder("/OEs/{id}/OEs/{targetId}", io.skysail.server.app.oEService.oe.OEsOEResource.class));
        router.attach(new RouteBuilder("/Users/{id}", io.skysail.server.app.oEService.user.resources.UserResourceGen.class));
        router.attach(new RouteBuilder("/Users/", io.skysail.server.app.oEService.user.resources.PostUserResourceGen.class));
        router.attach(new RouteBuilder("/Users/{id}/", io.skysail.server.app.oEService.user.resources.PutUserResourceGen.class));
        router.attach(new RouteBuilder("/Users", io.skysail.server.app.oEService.user.resources.UsersResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.oEService.user.resources.UsersResourceGen.class));
        router.attach(new RouteBuilder("/Users/{id}/OEs", io.skysail.server.app.oEService.user.UsersOEsResource.class));
        router.attach(new RouteBuilder("/Users/{id}/OEs/", io.skysail.server.app.oEService.user.PostUserToNewOERelationResource.class));
        router.attach(new RouteBuilder("/Users/{id}/OEs/{targetId}", io.skysail.server.app.oEService.user.UsersOEResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
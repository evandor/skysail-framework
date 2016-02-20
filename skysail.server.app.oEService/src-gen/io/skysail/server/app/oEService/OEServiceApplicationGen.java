package io.skysail.server.app.oEService;

import java.util.List;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;
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

        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users/{id}/OEs", io.skysail.server.app.oEService.UsersOEsResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users/{id}/OEs/", io.skysail.server.app.oEService.PostUsersOERelationResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.oEService.Users/{id}/OEs/{oeId}", io.skysail.server.app.oEService.UsersOEResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
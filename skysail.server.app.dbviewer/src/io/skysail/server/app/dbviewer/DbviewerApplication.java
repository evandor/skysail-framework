package io.skysail.server.app.dbviewer;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;
import io.skysail.server.app.dbviewer.dbclasses.*;
import io.skysail.server.app.dbviewer.dbentities.DbEntitiesResource;
import io.skysail.server.menus.MenuItemProvider;

import java.util.Arrays;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component(immediate = true)
public class DbviewerApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "dbviewer";

    private ConnectionRepo connectionsRepo;

    public DbviewerApplication() {
         super(APP_NAME, new ApiVersion(1), Arrays.asList(Connection.class));
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setRepositories(Repositories repos) {
       super.setRepositories(repos);
    }

    public void unsetRepositories(DbRepository repo) {
        super.setRepositories(null);
    }


    @Override
    protected void attach() {
      // super.attach();
       router.attach(new RouteBuilder("", ConnectionsResource.class));
       router.attach(new RouteBuilder("/connections", ConnectionsResource.class));
       router.attach(new RouteBuilder("/connections/", PostConnectionResource.class));
       router.attach(new RouteBuilder("/connections/{connectionId}", ConnectionResource.class));
       router.attach(new RouteBuilder("/connections/{connectionId}/", PutConnectionResource.class));

       router.attach(new RouteBuilder("/connections/{connectionId}/vertices", DbClassesResource.class));
//       router.attach(new RouteBuilder("/connections/", PostConnectionResource.class));
       router.attach(new RouteBuilder("/connections/{connectionId}/vertices/{id}", DbClassResource.class));
//       router.attach(new RouteBuilder("/connections/{id}/", PutConnectionResource.class));

       router.attach(new RouteBuilder("/connections/{connectionId}/vertices/{id}/entities", DbEntitiesResource.class));

    }

}






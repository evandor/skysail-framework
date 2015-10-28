package io.skysail.server.app.dbviewer;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.dbviewer.dbclasses.*;
import io.skysail.server.app.dbviewer.dbentities.DbEntitiesResource;
import io.skysail.server.menus.*;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component(immediate = true)
public class DbviewerApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "dbviewer";

    private ConnectionRepo connectionsRepo;

    public DbviewerApplication() {
         super(APP_NAME);
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=ConnectionRepository)")
    public void setConnectionsRepository(DbRepository repo) {
       this.connectionsRepo = (ConnectionRepo) repo;
    }

    public void unsetConnectionsRepository(DbRepository repo) {
        this.connectionsRepo = null;
    }

    @Override
    protected void attach() {
       super.attach();
       router.attach(new RouteBuilder("", ConnectionsResource.class));
       router.attach(new RouteBuilder("/connections", ConnectionsResource.class));
       router.attach(new RouteBuilder("/connections/", PostConnectionResource.class));
       router.attach(new RouteBuilder("/connections/{id}", ConnectionResource.class));
       router.attach(new RouteBuilder("/connections/{id}/", PutConnectionResource.class));

       router.attach(new RouteBuilder("/connections/{id}/vertices", DbClassesResource.class));
//       router.attach(new RouteBuilder("/connections/", PostConnectionResource.class));
       router.attach(new RouteBuilder("/connections/{id}/vertices/{name}", DbClassResource.class));
//       router.attach(new RouteBuilder("/connections/{id}/", PutConnectionResource.class));

       router.attach(new RouteBuilder("/connections/{id}/vertices/{name}/entities", DbEntitiesResource.class));

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    public ConnectionRepo getConnectionRepository() {
         return (ConnectionRepo) connectionsRepo;
    }

}






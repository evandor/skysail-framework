package io.skysail.server.app.wiki;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class WikiApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Wiki";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public WikiApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
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
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Spaces/{id}", io.skysail.server.app.wiki.SpaceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Spaces/", io.skysail.server.app.wiki.PostSpaceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Spaces/{id}/", io.skysail.server.app.wiki.PutSpaceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Spaces", io.skysail.server.app.wiki.SpacesResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.wiki.SpacesResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Pages/{id}", io.skysail.server.app.wiki.PageResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Pages/", io.skysail.server.app.wiki.PostPageResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Pages/{id}/", io.skysail.server.app.wiki.PutPageResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.wiki.Pages", io.skysail.server.app.wiki.PagesResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
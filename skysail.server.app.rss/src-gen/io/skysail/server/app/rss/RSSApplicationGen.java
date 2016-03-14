package io.skysail.server.app.rss;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class RSSApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "RSS";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public RSSApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
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
        router.attach(new RouteBuilder("/Feeds/{id}", io.skysail.server.app.rss.feed.resources.FeedResourceGen.class));
        router.attach(new RouteBuilder("/Feeds/", io.skysail.server.app.rss.feed.resources.PostFeedResourceGen.class));
        router.attach(new RouteBuilder("/Feeds/{id}/", io.skysail.server.app.rss.feed.resources.PutFeedResourceGen.class));
        router.attach(new RouteBuilder("/Feeds", io.skysail.server.app.rss.feed.resources.FeedsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.rss.feed.resources.FeedsResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
package io.skysail.server.app.rss;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.app.rss.feed.resources.*;
import io.skysail.server.ext.rome.RssClient;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;

@Component(immediate = true)
public class RSSApplication extends RSSApplicationGen implements ApplicationProvider, MenuItemProvider {
    
    @Reference
    @Getter
    private RssClient rssClient;

    public  RSSApplication() {
        super("RSS", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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
//        super.attach();
        router.attach(new RouteBuilder("/Feeds/{id}", FeedResource.class));
        router.attach(new RouteBuilder("/Feeds/", io.skysail.server.app.rss.feed.resources.PostFeedResourceGen.class));
        router.attach(new RouteBuilder("/Feeds/{id}/", io.skysail.server.app.rss.feed.resources.PutFeedResourceGen.class));
        router.attach(new RouteBuilder("/Feeds", FeedsResource.class));
        router.attach(new RouteBuilder("", FeedsResource.class));

        router.attach(new RouteBuilder("/Feeds/{id}/entries", FeedContentsResource.class));

    }

}

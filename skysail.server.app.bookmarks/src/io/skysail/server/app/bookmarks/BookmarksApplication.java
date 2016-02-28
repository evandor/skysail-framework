package io.skysail.server.app.bookmarks;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.bookmarks.resources.*;
import io.skysail.domain.core.Repositories;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;

@Component(immediate = true)
public class BookmarksApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "Bookmarks";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public BookmarksApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/book_link.png");
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", BookmarksResource.class));
        router.attach(new RouteBuilder("/", BookmarksResource.class));
        router.attach(new RouteBuilder("/bookmarks", BookmarksResource.class));
        router.attach(new RouteBuilder("/bookmarks/", PostBookmarkResource.class));
        router.attach(new RouteBuilder("/bookmarks/{id}", BookmarkResource.class));
        router.attach(new RouteBuilder("/bookmarks/{id}/", PutBookmarkResource.class));
    }
    
    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, unbind = "unsetRepositories")
    public void setRepositories(Repositories repos) {
       super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }


}

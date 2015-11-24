package io.skysail.server.app.bookmarks;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.bookmarks.resources.*;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.menus.MenuItemProvider;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@Component(immediate = true)
public class BookmarksApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "Bookmarks";

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

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setRepositories(Repositories repos) {
       super.setRepositories(repos);
    }

    public void unsetRepositories(DbRepository repo) {
        super.setRepositories(null);
    }


}

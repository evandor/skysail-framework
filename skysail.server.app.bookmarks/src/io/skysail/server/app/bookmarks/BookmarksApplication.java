package io.skysail.server.app.bookmarks;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.bookmarks.repo.BookmarksRepository;
import io.skysail.server.app.bookmarks.resources.*;
import io.skysail.server.menus.*;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
public class BookmarksApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "Bookmarks";
    private BookmarksRepository repo;

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

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=BookmarksRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (BookmarksRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.repo = null;
    }

    public BookmarksRepository getRepository() {
        return repo;
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

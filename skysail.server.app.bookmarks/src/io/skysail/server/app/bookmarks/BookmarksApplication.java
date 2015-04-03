package io.skysail.server.app.bookmarks;

import io.skysail.server.app.bookmarks.repo.BookmarksRepository;
import io.skysail.server.app.bookmarks.resources.BookmarksResource;
import io.skysail.server.app.bookmarks.resources.PostBookmarkResource;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class BookmarksApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "Bookmarks";
    private BookmarksRepository repo;

    public BookmarksApplication() {
        super(APP_NAME);
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", BookmarksResource.class));
        router.attach(new RouteBuilder("/", BookmarksResource.class));
        router.attach(new RouteBuilder("/bookmarks", BookmarksResource.class));
        router.attach(new RouteBuilder("/bookmarks/", PostBookmarkResource.class));
        // router.attach(new RouteBuilder("/clips/{id}", ClipResource.class));
        // router.attach(new RouteBuilder("/clips/{id}/",
        // PutClipResource.class));
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
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

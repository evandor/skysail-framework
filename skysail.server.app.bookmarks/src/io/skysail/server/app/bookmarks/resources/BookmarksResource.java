package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class BookmarksResource extends ListServerResource<Bookmark> {

    private BookmarksApplication app;

    @Override
    protected void doInit() {
        app = (BookmarksApplication) getApplication();
    }
 
    @Override
    public List<Bookmark> getEntity() {
       return app.getRepository().findAll(Bookmark.class, "ORDER BY name DESC");
    }
     
    @Override
    public List<Link> getLinkheader() {
       return super.getLinkheader(PostBookmarkResource.class);
    } 
 
}

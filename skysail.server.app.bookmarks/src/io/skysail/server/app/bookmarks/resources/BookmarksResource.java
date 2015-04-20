package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class BookmarksResource extends ListServerResource<Bookmark> {

    private BookmarksApplication app;
    
    public BookmarksResource() {
        super(BookmarkResource.class);
    }

    @Override
    protected void doInit() {
        app = (BookmarksApplication) getApplication();
    }
 
    @Override
    public List<Bookmark> getEntity() {
       return app.getRepository().findAll(Bookmark.class, "ORDER BY name DESC");
    }
     
    @Override
    public List<Link> getLinks() {
       return super.getLinks(PostBookmarkResource.class);
    } 
 
}

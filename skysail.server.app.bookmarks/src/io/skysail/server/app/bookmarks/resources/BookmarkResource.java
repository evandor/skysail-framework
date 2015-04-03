package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class BookmarkResource extends EntityServerResource<Bookmark> {

    private BookmarksApplication app;
    private String id;

    protected void doInit() {
        id = getAttribute("id");
        app = (BookmarksApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        app.getRepository().delete(Bookmark.class, id);
        return new SkysailResponse<>();
    }

    @Override
    public Bookmark getEntity() {
        return app.getRepository().getById(Bookmark.class, id);
    }
    
    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PutBookmarkResource.class);
    }

}

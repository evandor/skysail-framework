package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.bookmarks.*;
import io.skysail.server.app.bookmarks.repo.BookmarksRepository;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class BookmarksResource extends ListServerResource<Bookmark> {

    private BookmarksApplication app;
    private BookmarksRepository repository;

    public BookmarksResource() {
        super(BookmarkResource.class);
    }

    @Override
    protected void doInit() {
        app = (BookmarksApplication) getApplication();
        repository = (BookmarksRepository) app.getRepository(Bookmark.class);
    }

    @Override
    public List<Bookmark> getEntity() {
       return null;//repository.find("ORDER BY name DESC");
    }

    @Override
    public List<Link> getLinks() {
       return super.getLinks(PostBookmarkResource.class);
    }

}

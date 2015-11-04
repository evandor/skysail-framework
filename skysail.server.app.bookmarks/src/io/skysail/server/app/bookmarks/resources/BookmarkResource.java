package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bookmarks.*;
import io.skysail.server.app.bookmarks.repo.BookmarksRepository;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class BookmarkResource extends EntityServerResource<Bookmark> {

    private BookmarksApplication app;
    private String id;
    private BookmarksRepository repository;

    protected void doInit() {
        id = getAttribute("id");
        app = (BookmarksApplication) getApplication();
        repository = (BookmarksRepository) app.getRepository(Bookmark.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Bookmark getEntity() {
        return repository.findOne(id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutBookmarkResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }

}

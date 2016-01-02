package io.skysail.server.app.bookmarks.resources;

import io.skysail.server.app.bookmarks.*;
import io.skysail.server.app.bookmarks.repo.BookmarksRepository;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutBookmarkResource extends PutEntityServerResource<Bookmark> {

    private BookmarksApplication app;
    private String id;
    private BookmarksRepository repository;

    protected void doInit() {
        super.doInit();
        id = getAttribute("id");
        app = (BookmarksApplication) getApplication();
        repository = (BookmarksRepository) app.getRepository(Bookmark.class);
      }

      public Bookmark getEntity() {
         return repository.findOne(id);
      }

      public void updateEntity(Bookmark entity) {
         app.getRepository().update(entity.getId(), entity);
      }
}
package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutBookmarkResource extends PutEntityServerResource<Bookmark> {

    private BookmarksApplication app;
    private String id;

    protected void doInit() {
        super.doInit();
        id = getAttribute("id");
        app = (BookmarksApplication) getApplication();
      }
      
      public Bookmark getEntity() {
         return app.getRepository().getById(Bookmark.class, id);
      }
      
      public SkysailResponse<?> updateEntity(Bookmark entity) {
         app.getRepository().update(entity.getId(), entity);
         return new SkysailResponse<>();
      }
}
package io.skysail.server.app.bookmarks.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;

public class PostBookmarkResource extends PostEntityServerResource<Bookmark> {

    private BookmarksApplication app;

    @Override
    public void doInit() {
        app = (BookmarksApplication) getApplication();
    }

    @Override
    public Bookmark createEntityTemplate() {
        return new Bookmark();
    }

    @Override
    public SkysailResponse<?> addEntity(Bookmark entity) {
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}

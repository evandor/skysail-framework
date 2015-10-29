package io.skysail.server.app.um.db.users.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class UserResource extends EntityServerResource<User> {

    private UmApplication app;

    protected void doInit() {
        app = (UmApplication) getApplication();
    }

    public User getEntity() {
        return app.getUserRepository().getById(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutUserResource.class);
    }

    public SkysailResponse<User> eraseEntity() {
        app.getUserRepository().delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResource.class);
    }
}

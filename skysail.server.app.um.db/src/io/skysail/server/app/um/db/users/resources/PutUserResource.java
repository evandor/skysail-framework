package io.skysail.server.app.um.db.users.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutUserResource extends PutEntityServerResource<User> {

    private UmApplication app;

    protected void doInit() {
        super.doInit();
        app = (UmApplication) getApplication();
    }

    public User getEntity() {
        return app.getUserRepository().findOne(getAttribute("id"));
    }

    public SkysailResponse<User> updateEntity(User entity) {
        app.getUserRepository().update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResource.class);
    }
}

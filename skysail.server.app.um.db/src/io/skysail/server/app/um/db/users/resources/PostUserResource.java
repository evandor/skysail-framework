package io.skysail.server.app.um.db.users.resources;

import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostUserResource extends PostEntityServerResource<User> {

    private UmApplication app;

    @Override
    protected void doInit() {
        super.doInit();
        app = (UmApplication)getApplication();
    }

    @Override
    public User createEntityTemplate() {
        return new User();
    }

    @Override
    public void addEntity(User entity) {
        String id = app.getUserRepository().save(entity, app.getApplicationModel()).getId().toString();
        entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResource.class);
    }
}


package io.skysail.server.app.um.db.users.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

public class PostUserResource extends PostEntityServerResource<User> {

    private UmApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (UmApplication)getApplication();
    }

    @Override
    public User createEntityTemplate() {
        return new User();
    }

    @Override
    public SkysailResponse<User> addEntity(User entity) {

//        Role role = app.getRoleRepo().getById(roles);
//        entity.getRoles().add(role);

        String id = app.getRepository().save(entity, "roles").toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResource.class);
    }
}


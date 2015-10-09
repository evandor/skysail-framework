package io.skysail.server.app.um.db.users.resources;

import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostUserResource extends PostEntityServerResource<User> {

    @Override
    public User createEntityTemplate() {
        return new User();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResource.class);
    }
}


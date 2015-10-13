package io.skysail.server.app.um.db.users.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.*;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class UsersResource extends ListServerResource<User> {

    private UmApplication app;

    public UsersResource() {
        super(UserResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Users");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (UmApplication) getApplication();
    }

    @Override
    public List<User> getEntity() {
      return app.getRepository().findVertex(new Filter(getRequest()));
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        links.add(PostUserResource.class);
        return super.getLinks(links);
    }


}

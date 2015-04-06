package io.skysail.server.app.todos;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ListsResource.class);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

}
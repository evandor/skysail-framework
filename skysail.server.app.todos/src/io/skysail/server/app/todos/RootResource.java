package io.skysail.server.app.todos;

import io.skysail.api.links.Link;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<Identifiable> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ListsResource.class);
    }

    @Override
    public List<Identifiable> getEntity() {
        return null;
    }

}
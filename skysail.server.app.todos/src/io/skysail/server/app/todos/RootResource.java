package io.skysail.server.app.todos;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.lists.ListsResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ListsResource.class);
    }

    @Override
    public List<String> getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
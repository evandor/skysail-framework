package io.skysail.server.app.designer;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.Link;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<Identifiable> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ApplicationsResource.class);
    }

    @Override
    public List<Identifiable> getEntity() {
        return null;
    }

}
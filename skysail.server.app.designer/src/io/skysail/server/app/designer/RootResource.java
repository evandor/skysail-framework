package io.skysail.server.app.designer;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ApplicationsResource.class);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

}
package io.skysail.server.app.designer;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class RootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ApplicationsResource.class);
    }

    @Override
    public List<String> getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
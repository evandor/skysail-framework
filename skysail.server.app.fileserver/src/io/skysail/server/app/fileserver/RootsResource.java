package io.skysail.server.app.fileserver;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootsResource extends ListServerResource<RootPath> {

    public RootsResource() {
        super(ListFilesResource.class);
    }

    @Override
    public List<RootPath> getEntity() {
        return ((FileserverApplication)getApplication()).getRootPaths();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ListFilesResource.class);
    }
}

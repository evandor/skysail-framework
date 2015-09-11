package io.skysail.server.app.plugins.obr;

import io.skysail.api.links.Link;
import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class RepositoriesResource extends ListServerResource<ObrRepository> {

    private PluginApplication app;

    public RepositoriesResource() {
        super(RepositoryResource.class);
        app = (PluginApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Repositories");
    }

    @Override
    public List<ObrRepository> getEntity() {
        return app.getReposList();
    }

    @Override
    public List<Link> getLinks() {
        List<Link> links = super.getLinks(app.getMainLinks());
        //links.addAll(super.getLinks(RepositoriesResource.class));
        return links;
    }

}

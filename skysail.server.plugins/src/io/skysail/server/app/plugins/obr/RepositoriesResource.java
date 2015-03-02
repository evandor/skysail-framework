package io.skysail.server.app.plugins.obr;

import io.skysail.server.app.plugins.PluginApplication;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class RepositoriesResource extends ListServerResource<ObrRepository> {

    private PluginApplication app;

    public RepositoriesResource() {
        super(RepositoryResource.class);
        app = (PluginApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Repositories");
    }

    // @Override
    // public List<ObrRepository> getData() {
    // return app.getReposList();
    // }

}

package io.skysail.server.app.plugins.obr;

import io.skysail.server.app.plugins.PluginApplication;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class RepositoryResource extends EntityServerResource<ObrRepository> {

    private PluginApplication app;
    private String name;

    public RepositoryResource() {
        app = (PluginApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        name = (String) getRequest().getAttributes().get("id");
    }

    // @Override
    // public ObrRepository getData() {
    // return app.getReposList().stream().filter(r ->
    // r.getName().equals(name)).findFirst().orElse(null);
    // }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public ObrRepository getEntity() {
        return null;
    }

}

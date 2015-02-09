package io.skysail.server.app.plugins.query;

import io.skysail.server.app.plugins.PluginApplication;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostQueryResource extends PostEntityServerResource<Query> {

    private PluginApplication app;

    public PostQueryResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Search repositories");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PluginApplication) getApplication();
    }

    @Override
    public Query createEntityTemplate() {
        return new Query();
    }

    public Query getData(Form form) {
        return populate(createEntityTemplate(), form);
    }

    public SkysailResponse<?> addEntity(Query entity) {
        // Resolver discoverResources =
        // app.discoverResources("(|(presentationname=*)(symbolicname=*))");
        return new SkysailResponse<String>();
    }

}

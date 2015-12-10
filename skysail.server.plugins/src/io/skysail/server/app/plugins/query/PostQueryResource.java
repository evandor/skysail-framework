package io.skysail.server.app.plugins.query;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

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

    public void addEntity(Query entity) {
    }

}

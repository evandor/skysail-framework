package io.skysail.server.app.wiki.spaces;

import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class SpacesResource extends ListServerResource<Space> {

    private WikiApplication app;

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<Space> getEntity() {
        return app.getRepository().findAll(Space.class);
    }
}

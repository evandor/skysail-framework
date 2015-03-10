package io.skysail.server.app.wiki.spaces;

import io.skysail.server.app.wiki.WikiApplication;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

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

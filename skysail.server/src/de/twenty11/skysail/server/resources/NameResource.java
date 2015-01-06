package de.twenty11.skysail.server.resources;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class NameResource extends EntityServerResource<String> {

    private SkysailRootApplication app;

    public NameResource() {
        app = (SkysailRootApplication) getApplication();
    }

    @Override
    public String getData() {
        return "productName"; //app.getConfigForKey("productName");
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

}

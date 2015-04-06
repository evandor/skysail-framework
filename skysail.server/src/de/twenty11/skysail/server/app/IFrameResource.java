package de.twenty11.skysail.server.app;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.restlet.resource.ResourceException;

public class IFrameResource extends EntityServerResource<String> {

    private String url;

    public IFrameResource() {
    }

    @Override
    protected void doInit() throws ResourceException {
        url = getQueryValue("url");
    }

    @Override
    public String getEntity() {
        return url;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}

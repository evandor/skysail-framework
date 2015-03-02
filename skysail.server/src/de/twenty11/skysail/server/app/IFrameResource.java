package de.twenty11.skysail.server.app;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class IFrameResource extends EntityServerResource<String> {

    private String url;

    public IFrameResource() {
        // TODO Auto-generated constructor stub
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

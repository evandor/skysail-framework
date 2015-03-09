package io.skysail.server.app.designer.application;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class ApplicationResource extends EntityServerResource<Application> {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Application getEntity() {
        return null;
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutApplicationResource.class);
    }

}

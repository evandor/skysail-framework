package de.twenty11.skysail.server.resources;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class WelcomeResource extends EntityServerResource<String> {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public String getEntity() {
        return "";
    }

}
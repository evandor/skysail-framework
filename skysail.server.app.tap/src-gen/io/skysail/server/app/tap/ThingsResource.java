package io.skysail.server.app.tap;

import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ThingsResource extends ListServerResource<Thing> {

    public ThingsResource() {
        super(ThingResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Things");
    }

    @Override
    public List<Thing> getEntity() {
        return null;
    }
}

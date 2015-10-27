package io.skysail.server.app.dbviewer.vertices;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class VerticesResource extends ListServerResource<DbVertex> {

    public VerticesResource() {
        addToContext(ResourceContextId.LINK_TITLE, "show Vertices");
    }

    @Override
    public List<DbVertex> getEntity() {
        return Arrays.asList(new DbVertex());
    }

}

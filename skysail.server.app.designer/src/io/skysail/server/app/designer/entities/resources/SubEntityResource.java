package io.skysail.server.app.designer.entities.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SubEntityResource extends EntityServerResource<DbEntity> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public DbEntity getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }

}

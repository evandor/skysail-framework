package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

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
        return super.getLinks(PostFieldResource.class);
    }

}

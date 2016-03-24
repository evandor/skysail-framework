package io.skysail.server.app.designer.valueobjects.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ValueObjectResource extends EntityServerResource<DbValueObject> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public DbValueObject getEntity() {
        return null;
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(ValueObjectElementsResource.class, PostValueObjectElementResource.class, PutValueObjectResource.class);
    }

}

package io.skysail.server.app.designer.valueobjects.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.valueobjects.DbValueObjectElement;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ValueObjectElementResource extends EntityServerResource<DbValueObjectElement> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public DbValueObjectElement getEntity() {
        return null;
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutValueObjectElementResource.class);
    }

}

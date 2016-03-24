package io.skysail.server.designer.demo.apps.demoapplication;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


/**
 * generated from targetRelationResource.stg
 */
public class DemoApplicationsDemoValueObjectResource extends EntityServerResource<DemoValueObject> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public DemoValueObject getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DemoApplicationsDemoValueObjectsResource.class, PostDemoApplicationsDemoValueObjectRelationResource.class);
    }

}
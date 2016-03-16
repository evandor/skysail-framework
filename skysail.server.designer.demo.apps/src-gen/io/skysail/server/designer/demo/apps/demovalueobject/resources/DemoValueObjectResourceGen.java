package io.skysail.server.designer.demo.apps.demovalueobject.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


/**
 * generated from entityResourceNonAggregate.stg
 */
public class DemoValueObjectResourceGen extends EntityServerResource<io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject> {

    private String id;
    private AppsApplication app;
    //private DemoValueObjectRepository repository;

    public DemoValueObjectResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (AppsApplication) getApplication();
       // repository = (DemoValueObjectRepository) app.getRepository(io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        //repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject getEntity() {
        return (io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutDemoValueObjectResourceGen.class);
    }

}
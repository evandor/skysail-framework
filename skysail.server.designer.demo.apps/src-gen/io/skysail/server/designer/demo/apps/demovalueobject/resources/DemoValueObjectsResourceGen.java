package io.skysail.server.designer.demo.apps.demovalueobject.resources;

import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;



/**
 * generated from listResourceNonAggregate.stg
 */
public class DemoValueObjectsResourceGen extends ListServerResource<io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject> {

    private AppsApplication app;

    public DemoValueObjectsResourceGen() {
        super(DemoValueObjectResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list DemoValueObjects");
    }

    @Override
    protected void doInit() {
        app = (AppsApplication) getApplication();
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(DemoValueObject.class) + " WHERE #" + getAttribute("id") + " IN in('pages')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(DemoValueObject.class, sql);   
    }

    public List<Link> getLinks() {
       return super.getLinks(PostDemoValueObjectResourceGen.class);
    }
}
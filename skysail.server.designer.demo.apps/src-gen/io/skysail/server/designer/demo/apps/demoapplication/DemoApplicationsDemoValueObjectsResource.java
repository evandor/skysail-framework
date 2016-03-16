package io.skysail.server.designer.demo.apps.demoapplication;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


/**
 * generated from relationResource.stg
 */
public class DemoApplicationsDemoValueObjectsResource extends ListServerResource<DemoValueObject> {

    private AppsApplicationGen app;
    private DemoValueObjectRepository oeRepo;

    public DemoApplicationsDemoValueObjectsResource() {
        super(DemoApplicationResourceGen.class);//, DemoApplicationsDemoApplicationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (AppsApplication) getApplication();
        oeRepo = (DemoValueObjectRepository) app.getRepository(DemoValueObject.class);
    }

    @Override
    public List<DemoValueObject> getEntity() {
        return (List<DemoValueObject>) oeRepo.execute(DemoValueObject.class, "select * from " + DbClassName.of(DemoValueObject.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DemoApplicationsDemoValueObjectsResource.class, PostDemoApplicationsDemoValueObjectRelationResource.class);
    }
}
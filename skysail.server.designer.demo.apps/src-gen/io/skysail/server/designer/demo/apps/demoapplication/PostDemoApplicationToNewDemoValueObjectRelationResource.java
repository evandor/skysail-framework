package io.skysail.server.designer.demo.apps.demoapplication;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.PostRelationResource2;
import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostDemoApplicationToNewDemoValueObjectRelationResource extends PostRelationResource2<DemoValueObject> {

    private AppsApplicationGen app;
    private DemoApplicationRepository repo;
    private String parentId;

    public PostDemoApplicationToNewDemoValueObjectRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (AppsApplication) getApplication();
        repo = (DemoApplicationRepository) app.getRepository(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication.class);
        parentId = getAttribute("id");
    }

    public DemoValueObject createEntityTemplate() {
        return new DemoValueObject();
    }

    @Override
    public void addEntity(DemoValueObject entity) {
        DemoApplication parent = repo.findOne(parentId);
        parent.getDemoValueObjects().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DemoApplicationsDemoValueObjectsResource.class, PostDemoApplicationToNewDemoValueObjectRelationResource.class);
    }
}
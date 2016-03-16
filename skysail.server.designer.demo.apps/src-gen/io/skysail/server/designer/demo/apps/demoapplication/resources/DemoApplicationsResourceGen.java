package io.skysail.server.designer.demo.apps.demoapplication.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


/**
 * generated from listResource.stg
 */
public class DemoApplicationsResourceGen extends ListServerResource<io.skysail.server.designer.demo.apps.demoapplication.DemoApplication> {

    private AppsApplication app;
    private DemoApplicationRepository repository;

    public DemoApplicationsResourceGen() {
        super(DemoApplicationResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list DemoApplications");
    }

    public DemoApplicationsResourceGen(Class<? extends DemoApplicationResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (AppsApplication) getApplication();
        repository = (DemoApplicationRepository) app.getRepository(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.designer.demo.apps.demoapplication.DemoApplication> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostDemoApplicationResourceGen.class,DemoApplicationsResourceGen.class);
    }
}
package io.skysail.server.designer.demo.apps.demoapplication;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


/**
 * generated from postRelationResource.stg
 */
public class PostDemoApplicationsDemoValueObjectRelationResource extends PostRelationResource<io.skysail.server.designer.demo.apps.demoapplication.DemoApplication, io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject> {

    private AppsApplicationGen app;
    private DemoValueObjectRepository DemoValueObjectRepo;
    private DemoApplicationRepository DemoApplicationRepo;

    public PostDemoApplicationsDemoValueObjectRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (AppsApplicationGen) getApplication();
        DemoValueObjectRepo = (DemoValueObjectRepository) app.getRepository(io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<DemoValueObject> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), DemoValueObjectRepo.count(filter));
        return DemoValueObjectRepo.find(filter, pagination);
    }

    @Override
    protected List<DemoValueObject> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), DemoValueObjectRepo.count(filter));
        return DemoValueObjectRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<DemoValueObject> entities) {
        String id = getAttribute("id");
        io.skysail.server.designer.demo.apps.demoapplication.DemoApplication theUser = DemoApplicationRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        DemoValueObjectRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication theUser, DemoValueObject e) {
        if (!theUser.getDemoValueObjects().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getDemoValueObjects().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
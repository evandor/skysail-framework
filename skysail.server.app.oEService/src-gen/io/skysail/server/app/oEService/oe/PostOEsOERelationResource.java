package io.skysail.server.app.oEService.oe;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

import io.skysail.server.app.oEService.*;

import io.skysail.server.app.oEService.oe.*;
import io.skysail.server.app.oEService.oe.resources.*;
import io.skysail.server.app.oEService.user.*;
import io.skysail.server.app.oEService.user.resources.*;


/**
 * generated from postSelfRelationResource.stg
 */
public class PostOEsOERelationResource extends PostRelationResource<io.skysail.server.app.oEService.oe.OE, io.skysail.server.app.oEService.oe.OE> {

    private OEServiceApplicationGen app;
    private OERepository OERepo;

    public PostOEsOERelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplicationGen) getApplication();
        OERepo = (OERepository) app.getRepository(io.skysail.server.app.oEService.oe.OE.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<OE> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), OERepo.count(filter));
        return OERepo.find(filter, pagination);
    }

    @Override
    protected List<OE> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), OERepo.count(filter));
        return OERepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<OE> entities) {
        String id = getAttribute("id");
        io.skysail.server.app.oEService.oe.OE theUser = OERepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        OERepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.app.oEService.oe.OE theUser, OE e) {
        if (!theUser.getOEs().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getOEs().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
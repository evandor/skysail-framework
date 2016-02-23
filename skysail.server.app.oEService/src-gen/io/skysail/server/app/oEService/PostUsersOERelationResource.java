package io.skysail.server.app.oEService;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

public class PostUsersOERelationResource extends PostRelationResource<io.skysail.server.app.oEService.OE, io.skysail.server.app.oEService.OE> {

    private OEServiceApplication app;
    private OERepository oeRepo;
    private UserRepository userRepo;

    public PostUsersOERelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        oeRepo = (OERepository) app.getRepository(io.skysail.server.app.oEService.OE.class);
        userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<OE> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), oeRepo.count(filter));
        return oeRepo.find(filter, pagination);
    }

    @Override
    protected List<OE> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), oeRepo.count(filter));
        return oeRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<OE> entities) {
        String id = getAttribute("id");
        User theUser = userRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        userRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(User theUser, OE e) {
        if (!theUser.getOEs().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getOEs().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
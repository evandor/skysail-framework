package io.skysail.server.app.oEService;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

public class PostUsersOERelationResource extends PostRelationResource<User, OE> {

    private OEServiceApplication app;
    private OERepository repository;

    public PostUsersOERelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        repository = (OERepository) app.getRepository(io.skysail.server.app.oEService.OE.class);
    }

    @Override
    public List<OE> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    protected List<OE> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);//.stream().filter(predicate);
    }
    
    @Override
    public void addRelations(Object entity) {
        String attribute = getAttribute("id");
        System.out.println(attribute);
        System.out.println(entity);
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }
    
    

}

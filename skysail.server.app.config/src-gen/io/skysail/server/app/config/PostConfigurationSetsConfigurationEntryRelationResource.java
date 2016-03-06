package io.skysail.server.app.config;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

/**
 * generated from postRelationResource.stg
 */
public class PostConfigurationSetsConfigurationEntryRelationResource extends PostRelationResource<io.skysail.server.app.config.ConfigurationSet, > {

    private ConfigApplication app;
    private ConfigurationEntryRepository repo;
    //private UserRepository userRepo;

    public PostConfigurationSetsConfigurationEntryRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (ConfigApplication) getApplication();
        repo = (ConfigurationEntryRepository) app.getRepository(.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<ConfigurationEntry> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repo.count(filter));
        return repo.find(filter, pagination);
    }

    @Override
    protected List<ConfigurationEntry> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repo.count(filter));
        return repo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<ConfigurationEntry> entities) {
        String id = getAttribute("id");
        io.skysail.server.app.config.ConfigurationSet theUser = repo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        repo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.app.config.ConfigurationSet theUser, ConfigurationEntry e) {
        if (!theUser.getConfigurationEntrys().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getConfigurationEntrys().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
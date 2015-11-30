package io.skysail.server.app.wiki.repository;

import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=SpacesRepository")
public class SpacesRepository extends GraphDbRepository<Space> implements DbRepository {

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", Space.class.getSimpleName());
        dbService.register(Space.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }





}

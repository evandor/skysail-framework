package io.skysail.server.app.wiki.repository;

import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.db.*;
import io.skysail.server.repo.DbRepository;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=SpacesRepository")
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

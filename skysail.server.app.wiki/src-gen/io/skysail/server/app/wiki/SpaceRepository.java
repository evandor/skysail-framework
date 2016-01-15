package io.skysail.server.app.wiki;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=SpacesRepository")
public class SpaceRepository extends GraphDbRepository<io.skysail.server.app.wiki.Space> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.wiki.Space" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.wiki.Space.class));
        dbService.register(Space.class);
    }

}
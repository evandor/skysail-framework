package io.skysail.server.app.tap;

import javax.annotation.Generated;

import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=PlaceRepository")
@Slf4j
@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PlaceRepo extends GraphDbRepository<Place> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(Place.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        log.debug("setting dbService");
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        log.debug("unsetting dbService");
        this.dbService = null;
    }
}




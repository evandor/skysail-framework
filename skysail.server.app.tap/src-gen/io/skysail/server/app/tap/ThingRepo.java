package io.skysail.server.app.tap;


import javax.annotation.Generated;

import io.skysail.api.repos.DbRepository;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;
import io.skysail.server.db.*;

@Component(immediate = true, properties = "name=ThingRepository")
@Slf4j
@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ThingRepo extends GraphDbRepository<Thing> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(Thing.class);
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




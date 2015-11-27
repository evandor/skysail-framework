package io.skysail.server.designer.presentation;

import javax.annotation.Generated;

import io.skysail.api.repos.DbRepository;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;
import io.skysail.server.db.*;

@Component(immediate = true, properties = "name=TopicRepository")
@Slf4j
@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class TopicRepo extends GraphDbRepository<Topic> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(Topic.class);
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



package io.skysail.server.db.versions.impl;

import io.skysail.server.db.*;
import io.skysail.server.repo.DbRepository;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=VersionsRepository")
@Slf4j
public class VersionsRepository extends GraphDbRepository<ComponentDbVersion> implements DbRepository {

    @Activate
    public void activate() {
        log.info("activating VersionsRepository");
        dbService.createWithSuperClass("V", ComponentDbVersion.class.getSimpleName());
        dbService.register(ComponentDbVersion.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    public Object execute(String statement) {
        return dbService.executeUpdate(statement, Collections.emptyMap());
    }

}

package io.skysail.server.db.versions.impl;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=VersionsRepository")
@Slf4j
public class VersionsRepository extends GraphDbRepository<ComponentDbVersion> implements DbRepository {

    @Activate
    public void activate() {
        log.info("activating VersionsRepository");
        dbService.createWithSuperClass("V", DbClassName.of(ComponentDbVersion.class));
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
        return dbService.executeUpdateVertex(statement, Collections.emptyMap());
    }

}

package io.skysail.server.db.it;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

import org.osgi.service.component.annotations.*;

@Component(immediate = true, properties = "name=TestRepository")
@Slf4j
public class TestRepository extends GraphDbRepository<TestEntity>  implements DbRepository {

    @Activate
    public void activate() {
        super.activate(TestEntity.class);
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
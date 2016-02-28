package io.skysail.server.app.snap;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=ModelsRepository")
public class ModelRepository extends GraphDbRepository<io.skysail.server.app.snap.Model> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.snap.Model" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.snap.Model.class));
        dbService.register(Model.class);
    }

}
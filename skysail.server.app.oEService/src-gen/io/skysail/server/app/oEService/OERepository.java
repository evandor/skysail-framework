package io.skysail.server.app.oEService;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=OEsRepository")
public class OERepository extends GraphDbRepository<io.skysail.server.app.oEService.OE> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.oEService.OE" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.oEService.OE.class));
        dbService.register(OE.class);
    }

}
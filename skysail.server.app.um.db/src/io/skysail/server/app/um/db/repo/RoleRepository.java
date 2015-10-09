package io.skysail.server.app.um.db.repo;

import io.skysail.server.app.um.db.domain.Role;
import io.skysail.server.db.*;
import io.skysail.server.repo.DbRepository;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=RoleRepository")
@Slf4j
public class RoleRepository extends GraphDbRepository<Role>  implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        log.debug("activating repository");
        dbService.createWithSuperClass("V", Role.class.getSimpleName());
        dbService.register(Role.class);
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

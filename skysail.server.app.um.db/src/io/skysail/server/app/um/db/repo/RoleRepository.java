package io.skysail.server.app.um.db.repo;

import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.um.db.domain.Role;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=RoleRepository")
@Slf4j
public class RoleRepository extends GraphDbRepository<Role>  implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        super.activate(Role.class);
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

package io.skysail.server.app.um.db.repo;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.um.db.domain.Permission;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=PermissionRepository")
@Slf4j
public class PermissionRepository extends GraphDbRepository<Permission>  implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        super.activate(Permission.class);
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

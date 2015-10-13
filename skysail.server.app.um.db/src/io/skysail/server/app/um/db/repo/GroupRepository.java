package io.skysail.server.app.um.db.repo;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.um.db.domain.Group;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=GroupRepository")
@Slf4j
public class GroupRepository extends GraphDbRepository<Group>  implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        super.activate(Group.class);
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

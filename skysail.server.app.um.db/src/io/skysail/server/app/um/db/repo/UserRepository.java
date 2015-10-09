package io.skysail.server.app.um.db.repo;

import io.skysail.server.app.um.db.domain.User;
import io.skysail.server.db.*;
import io.skysail.server.repo.DbRepository;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=UserRepository")
@Slf4j
public class UserRepository extends GraphDbRepository<User>  implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        log.debug("activating repository");
        dbService.createWithSuperClass("V", User.class.getSimpleName());
        dbService.register(User.class);
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

package io.skysail.server.db.it.clip;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=ClipRepository")
@Slf4j
public class ClipRepository extends GraphDbRepository<Clip> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(Clip.class);
        dbService.executeUpdate("DELETE vertex Clip", Collections.emptyMap());
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
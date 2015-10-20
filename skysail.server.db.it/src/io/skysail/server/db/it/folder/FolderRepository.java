package io.skysail.server.db.it.folder;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.*;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=FolderRepository")
@Slf4j
public class FolderRepository extends GraphDbRepository<Folder> implements DbRepository {

    @Activate
    public void activate() {
        super.activate(Folder.class);
        dbService.executeUpdate("DELETE vertex Folder", Collections.emptyMap());
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
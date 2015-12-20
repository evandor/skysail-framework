package io.skysail.server.db.it.folder;

import java.util.Collections;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=FolderRepository")
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
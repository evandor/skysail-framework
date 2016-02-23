package io.skysail.server.designer.demo.folders;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=FoldersRepository")
public class FolderRepository extends GraphDbRepository<io.skysail.server.designer.demo.folders.Folder> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.demo.folders.Folder" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.demo.folders.Folder.class));
        dbService.register(Folder.class);
    }

}
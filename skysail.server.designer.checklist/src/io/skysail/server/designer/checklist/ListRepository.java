package io.skysail.server.designer.checklist;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=ListsRepository")
public class ListRepository extends GraphDbRepository<io.skysail.server.designer.checklist.List> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.checklist.List" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.checklist.List.class));
        dbService.register(List.class);
    }

}
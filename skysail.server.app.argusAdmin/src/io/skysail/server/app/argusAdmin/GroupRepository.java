package io.skysail.server.app.argusAdmin;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=GroupsRepository")
public class GroupRepository extends GraphDbRepository<io.skysail.server.app.argusAdmin.Group> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.argusAdmin.Group" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.argusAdmin.Group.class));
        dbService.register(Group.class);
    }

}
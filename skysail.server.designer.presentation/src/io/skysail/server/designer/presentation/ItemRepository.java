package io.skysail.server.designer.presentation;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=ItemsRepository")
public class ItemRepository extends GraphDbRepository<io.skysail.server.designer.presentation.Item> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.presentation.Item" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.presentation.Item.class));
        dbService.register(Item.class);
    }

}
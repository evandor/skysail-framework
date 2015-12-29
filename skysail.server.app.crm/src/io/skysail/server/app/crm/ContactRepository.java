package io.skysail.server.app.cRM;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=ContactsRepository")
public class ContactRepository extends GraphDbRepository<io.skysail.server.app.cRM.Contact> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.cRM.Contact" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.cRM.Contact.class));
        dbService.register(Contact.class);
    }

}
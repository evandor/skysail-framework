package io.skysail.server.designer.demo.accounts;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

/**
 * generated from repository.stg
 */
@Component(immediate = true, property = "name=AccountsRepository")
public class AccountRepository extends GraphDbRepository<io.skysail.server.designer.demo.accounts.account.Account> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.demo.accounts.account.Account" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.demo.accounts.account.Account.class));
        dbService.register(io.skysail.server.designer.demo.accounts.account.Account.class);
    }

}
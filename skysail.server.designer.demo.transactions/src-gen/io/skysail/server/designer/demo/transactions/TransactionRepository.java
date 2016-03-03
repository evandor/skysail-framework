package io.skysail.server.designer.demo.transactions;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=TransactionsRepository")
public class TransactionRepository extends GraphDbRepository<io.skysail.server.designer.demo.transactions.Transaction> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.designer.demo.transactions.Transaction" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.designer.demo.transactions.Transaction.class));
        dbService.register(Transaction.class);
    }

}
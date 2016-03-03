package io.skysail.server.designer.demo.transactions.transaction.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.transactions.*;
import io.skysail.server.designer.demo.transactions.transaction.*;

/**
 * generated from putResource.stg
 */
public class PutTransactionResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.transactions.transaction.Transaction> {


    protected String id;
    protected TransactionsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (TransactionsApplication)getApplication();
    }

    @Override
    public void updateEntity(Transaction  entity) {
        io.skysail.server.designer.demo.transactions.transaction.Transaction original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.transactions.transaction.Transaction.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.transactions.transaction.Transaction getEntity() {
        return (io.skysail.server.designer.demo.transactions.transaction.Transaction)app.getRepository(io.skysail.server.designer.demo.transactions.transaction.Transaction.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }
}
package io.skysail.server.designer.demo.transactions;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

/**
 * generated from putResource.stg
 */
public class PutTransactionResource extends PutEntityServerResource<io.skysail.server.designer.demo.transactions.Transaction> {


    protected String id;
    protected TransactionsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (TransactionsApplication)getApplication();
    }

    @Override
    public void updateEntity(Transaction  entity) {
        io.skysail.server.designer.demo.transactions.Transaction original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.transactions.Transaction.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.transactions.Transaction getEntity() {
        return (io.skysail.server.designer.demo.transactions.Transaction)app.getRepository(io.skysail.server.designer.demo.transactions.Transaction.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResource.class);
    }
}
package io.skysail.server.designer.demo.accounts.transaction.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.accounts.*;
import io.skysail.server.designer.demo.accounts.transaction.*;

/**
 * generated from putResource.stg
 */
public class PutTransactionResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.accounts.transaction.Transaction> {


    protected String id;
    protected AccountsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (AccountsApplication)getApplication();
    }

    @Override
    public void updateEntity(Transaction  entity) {
        io.skysail.server.designer.demo.accounts.transaction.Transaction original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.accounts.transaction.Transaction.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.accounts.transaction.Transaction getEntity() {
        return (io.skysail.server.designer.demo.accounts.transaction.Transaction)app.getRepository(io.skysail.server.designer.demo.accounts.transaction.Transaction.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }
}
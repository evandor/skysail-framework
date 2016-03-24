package io.skysail.server.designer.demo.accounts.transaction.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.accounts.*;

import io.skysail.server.designer.demo.accounts.account.*;
import io.skysail.server.designer.demo.accounts.account.resources.*;
import io.skysail.server.designer.demo.accounts.transaction.*;
import io.skysail.server.designer.demo.accounts.transaction.resources.*;


/**
 * generated from entityResourceNonAggregate.stg
 */
public class TransactionResourceGen extends EntityServerResource<io.skysail.server.designer.demo.accounts.transaction.Transaction> {

    private String id;
    private AccountsApplication app;
    //private TransactionRepository repository;

    public TransactionResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (AccountsApplication) getApplication();
       // repository = (TransactionRepository) app.getRepository(io.skysail.server.designer.demo.accounts.transaction.Transaction.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        //repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.demo.accounts.transaction.Transaction getEntity() {
        return (io.skysail.server.designer.demo.accounts.transaction.Transaction)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTransactionResourceGen.class);
    }

}
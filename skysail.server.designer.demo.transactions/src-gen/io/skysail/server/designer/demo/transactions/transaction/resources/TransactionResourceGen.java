package io.skysail.server.designer.demo.transactions.transaction.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.designer.demo.transactions.*;

import io.skysail.server.designer.demo.transactions.transaction.*;
import io.skysail.server.designer.demo.transactions.transaction.resources.*;


/**
 * generated from entityResource.stg
 */
public class TransactionResourceGen extends EntityServerResource<io.skysail.server.designer.demo.transactions.transaction.Transaction> {

    private String id;
    private TransactionsApplication app;
    private TransactionRepository repository;

    public TransactionResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (TransactionsApplication) getApplication();
        repository = (TransactionRepository) app.getRepository(io.skysail.server.designer.demo.transactions.transaction.Transaction.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Transaction getEntity() {
        return (Transaction)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTransactionResourceGen.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }


}
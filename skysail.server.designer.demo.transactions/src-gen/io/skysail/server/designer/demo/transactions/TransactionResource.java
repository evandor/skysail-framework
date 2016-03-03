package io.skysail.server.designer.demo.transactions;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from entityResource.stg
 */
public class TransactionResource extends EntityServerResource<io.skysail.server.designer.demo.transactions.Transaction> {

    private String id;
    private TransactionsApplication app;
    private TransactionRepository repository;

    public TransactionResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (TransactionsApplication) getApplication();
        repository = (TransactionRepository) app.getRepository(io.skysail.server.designer.demo.transactions.Transaction.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.demo.transactions.Transaction getEntity() {
        return (io.skysail.server.designer.demo.transactions.Transaction)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTransactionResource.class);
    }

}
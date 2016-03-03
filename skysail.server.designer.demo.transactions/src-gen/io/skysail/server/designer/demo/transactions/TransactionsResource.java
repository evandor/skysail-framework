package io.skysail.server.designer.demo.transactions;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;

/**
 * generated from listResource.stg
 */
public class TransactionsResource extends ListServerResource<io.skysail.server.designer.demo.transactions.Transaction> {

    private TransactionsApplication app;
    private TransactionRepository repository;

    public TransactionsResource() {
        super(TransactionResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Transactions");
    }

    public TransactionsResource(Class<? extends TransactionResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (TransactionsApplication) getApplication();
        repository = (TransactionRepository) app.getRepository(io.skysail.server.designer.demo.transactions.Transaction.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.designer.demo.transactions.Transaction> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostTransactionResource.class,TransactionsResource.class);
    }
}
package io.skysail.server.designer.demo.transactions.transaction.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.transactions.*;

/**
 * generated from listResource.stg
 */
public class TransactionsResourceGen extends ListServerResource<io.skysail.server.designer.demo.transactions.transaction.Transaction> {

    private TransactionsApplication app;
    private TransactionRepository repository;

    public TransactionsResourceGen() {
        super(TransactionResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Transactions");
    }

    public TransactionsResourceGen(Class<? extends TransactionResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (TransactionsApplication) getApplication();
        repository = (TransactionRepository) app.getRepository(io.skysail.server.designer.demo.transactions.transaction.Transaction.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.designer.demo.transactions.transaction.Transaction> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostTransactionResourceGen.class,TransactionsResourceGen.class);
    }
}
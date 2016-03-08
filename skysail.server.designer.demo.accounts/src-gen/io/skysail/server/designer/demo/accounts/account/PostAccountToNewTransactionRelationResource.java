package io.skysail.server.designer.demo.accounts.account;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.PostRelationResource2;
import io.skysail.server.designer.demo.accounts.*;

import io.skysail.server.designer.demo.accounts.account.*;
import io.skysail.server.designer.demo.accounts.account.resources.*;
import io.skysail.server.designer.demo.accounts.transaction.*;
import io.skysail.server.designer.demo.accounts.transaction.resources.*;


/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostAccountToNewTransactionRelationResource extends PostRelationResource2<Transaction> {

    private AccountsApplicationGen app;
    private TransactionRepository repo;
    private String parentId;

    public PostAccountToNewTransactionRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (AccountsApplication) getApplication();
        repo = (TransactionRepository) app.getRepository(.class);
        parentId = getAttribute("id");
    }

    public Transaction createEntityTemplate() {
        return new Transaction();
    }

    @Override
    public void addEntity(Transaction entity) {
        Transaction parent = repo.findOne(parentId);
        parent.getTransactions().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsTransactionsResource.class);
    }
}
package io.skysail.server.designer.demo.accounts.transaction.resources;

import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.accounts.*;

import io.skysail.server.designer.demo.accounts.account.*;
import io.skysail.server.designer.demo.accounts.account.resources.*;
import io.skysail.server.designer.demo.accounts.transaction.*;
import io.skysail.server.designer.demo.accounts.transaction.resources.*;



/**
 * generated from listResourceNonAggregate.stg
 */
public class TransactionsResourceGen extends ListServerResource<io.skysail.server.designer.demo.accounts.transaction.Transaction> {

    private AccountsApplication app;

    public TransactionsResourceGen() {
        super(TransactionResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Transactions");
    }

    @Override
    protected void doInit() {
        app = (AccountsApplication) getApplication();
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Transaction.class) + " WHERE #" + getAttribute("id") + " IN in('pages')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(Transaction.class, sql);   
    }

    public List<Link> getLinks() {
       return super.getLinks(PostTransactionResourceGen.class);
    }
}
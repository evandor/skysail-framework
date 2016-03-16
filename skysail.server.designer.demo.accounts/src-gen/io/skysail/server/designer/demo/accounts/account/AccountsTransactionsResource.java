package io.skysail.server.designer.demo.accounts.account;

import java.util.Collections;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.accounts.AccountsApplication;
import io.skysail.server.designer.demo.accounts.AccountsApplicationGen;
import io.skysail.server.designer.demo.accounts.account.resources.AccountResourceGen;
import io.skysail.server.designer.demo.accounts.transaction.Transaction;
import io.skysail.server.restlet.resources.ListServerResource;


/**
 * generated from relationResource.stg
 */
public class AccountsTransactionsResource extends ListServerResource<Transaction> {

    private AccountsApplicationGen app;
   // private TransactionRepository oeRepo;

    public AccountsTransactionsResource() {
        super(AccountResourceGen.class);//, AccountsAccountResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (AccountsApplication) getApplication();
        //oeRepo = (TransactionRepository) app.getRepository(Transaction.class);
    }

    @Override
    public List<Transaction> getEntity() {
        return Collections.emptyList();//(List<Transaction>) oeRepo.execute(Transaction.class, "select * from " + DbClassName.of(Transaction.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsTransactionsResource.class, PostAccountsTransactionRelationResource.class);
    }
}
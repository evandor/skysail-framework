package io.skysail.server.designer.demo.accounts.account;

import java.util.Collections;
import java.util.List;

import io.skysail.server.designer.demo.accounts.AccountRepository;
import io.skysail.server.designer.demo.accounts.AccountsApplicationGen;
import io.skysail.server.designer.demo.accounts.transaction.Transaction;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.PostRelationResource;


/**
 * generated from postRelationResource.stg
 */
public class PostAccountsTransactionRelationResource extends PostRelationResource<io.skysail.server.designer.demo.accounts.account.Account, io.skysail.server.designer.demo.accounts.transaction.Transaction> {

    private AccountsApplicationGen app;
    //private TransactionRepository TransactionRepo;
    private AccountRepository AccountRepo;

    public PostAccountsTransactionRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (AccountsApplicationGen) getApplication();
        //TransactionRepo = (TransactionRepository) app.getRepository(io.skysail.server.designer.demo.accounts.transaction.Transaction.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<Transaction> getEntity() {
        Filter filter = new Filter(getRequest());
        //Pagination pagination = new Pagination(getRequest(), getResponse(), TransactionRepo.count(filter));
        return Collections.emptyList();//TransactionRepo.find(filter, pagination);
    }

    @Override
    protected List<Transaction> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        //Pagination pagination = new Pagination(getRequest(), getResponse(), TransactionRepo.count(filter));
        return Collections.emptyList();//TransactionRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<Transaction> entities) {
        String id = getAttribute("id");
        io.skysail.server.designer.demo.accounts.account.Account theUser = AccountRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
       // TransactionRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.designer.demo.accounts.account.Account theUser, Transaction e) {
        if (!theUser.getTransactions().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getTransactions().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
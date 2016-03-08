package io.skysail.server.designer.demo.accounts;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class AccountsApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Accounts";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public AccountsApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/Accounts/{id}", io.skysail.server.designer.demo.accounts.account.resources.AccountResourceGen.class));
        router.attach(new RouteBuilder("/Accounts/", io.skysail.server.designer.demo.accounts.account.resources.PostAccountResourceGen.class));
        router.attach(new RouteBuilder("/Accounts/{id}/", io.skysail.server.designer.demo.accounts.account.resources.PutAccountResourceGen.class));
        router.attach(new RouteBuilder("/Accounts", io.skysail.server.designer.demo.accounts.account.resources.AccountsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.accounts.account.resources.AccountsResourceGen.class));
        router.attach(new RouteBuilder("/Accounts/{id}/Transactions", io.skysail.server.designer.demo.accounts.account.AccountsTransactionsResource.class));
        router.attach(new RouteBuilder("/Accounts/{id}/Transactions/", io.skysail.server.designer.demo.accounts.account.PostAccountToNewTransactionRelationResource.class));
        router.attach(new RouteBuilder("/Accounts/{id}/Transactions/{targetId}", io.skysail.server.designer.demo.accounts.account.AccountsTransactionResource.class));
        router.attach(new RouteBuilder("/Transactions/{id}", io.skysail.server.designer.demo.accounts.transaction.resources.TransactionResourceGen.class));
        router.attach(new RouteBuilder("/Transactions/", io.skysail.server.designer.demo.accounts.transaction.resources.PostTransactionResourceGen.class));
        router.attach(new RouteBuilder("/Transactions/{id}/", io.skysail.server.designer.demo.accounts.transaction.resources.PutTransactionResourceGen.class));
        router.attach(new RouteBuilder("/Transactions", io.skysail.server.designer.demo.accounts.transaction.resources.TransactionsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.accounts.transaction.resources.TransactionsResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
package io.skysail.server.designer.demo.accounts.account.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.accounts.*;

import io.skysail.server.designer.demo.accounts.account.*;
import io.skysail.server.designer.demo.accounts.account.resources.*;
import io.skysail.server.designer.demo.accounts.transaction.*;
import io.skysail.server.designer.demo.accounts.transaction.resources.*;


/**
 * generated from listResource.stg
 */
public class AccountsResourceGen extends ListServerResource<io.skysail.server.designer.demo.accounts.account.Account> {

    private AccountsApplication app;
    private AccountRepository repository;

    public AccountsResourceGen() {
        super(AccountResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Accounts");
    }

    public AccountsResourceGen(Class<? extends AccountResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (AccountsApplication) getApplication();
        repository = (AccountRepository) app.getRepository(io.skysail.server.designer.demo.accounts.account.Account.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.designer.demo.accounts.account.Account> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostAccountResourceGen.class,AccountsResourceGen.class);
    }
}
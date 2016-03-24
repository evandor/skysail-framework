package io.skysail.server.designer.demo.accounts.transaction.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.accounts.*;

import io.skysail.server.designer.demo.accounts.account.*;
import io.skysail.server.designer.demo.accounts.account.resources.*;
import io.skysail.server.designer.demo.accounts.transaction.*;
import io.skysail.server.designer.demo.accounts.transaction.resources.*;



/**
 * generated from postResourceNonAggregate.stg
 */
public class PostTransactionResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.accounts.transaction.Transaction> {

	private AccountsApplication app;
    private Repository repository;

    public PostTransactionResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (AccountsApplication) getApplication();
        repository = null;//app.getRepository(Space.class);
    }

    @Override
    public io.skysail.server.designer.demo.accounts.transaction.Transaction createEntityTemplate() {
        return new Transaction();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.accounts.transaction.Transaction entity) {
        Subject subject = SecurityUtils.getSubject();

        io.skysail.server.designer.demo.accounts.account.Account entityRoot = (io.skysail.server.designer.demo.accounts.account.Account) repository.findOne(getAttribute("id"));
        entityRoot.getTransactions().add(entity);
        repository.update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }
}
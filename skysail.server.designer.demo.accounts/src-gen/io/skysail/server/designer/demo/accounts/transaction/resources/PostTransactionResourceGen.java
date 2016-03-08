package io.skysail.server.designer.demo.accounts.transaction.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.accounts.*;
import io.skysail.server.designer.demo.accounts.transaction.*;

/**
 * generated from postResource.stg
 */
public class PostTransactionResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.accounts.transaction.Transaction> {

	protected AccountsApplication app;

    public PostTransactionResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (AccountsApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.accounts.transaction.Transaction createEntityTemplate() {
        return new Transaction();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.accounts.transaction.Transaction entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.accounts.transaction.Transaction.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }
}
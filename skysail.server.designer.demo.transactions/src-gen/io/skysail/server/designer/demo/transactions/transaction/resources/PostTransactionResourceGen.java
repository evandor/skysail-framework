package io.skysail.server.designer.demo.transactions.transaction.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.transactions.*;
import io.skysail.server.designer.demo.transactions.transaction.*;

/**
 * generated from postResource.stg
 */
public class PostTransactionResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.transactions.transaction.Transaction> {

	protected TransactionsApplication app;

    public PostTransactionResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TransactionsApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.transactions.transaction.Transaction createEntityTemplate() {
        return new Transaction();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.transactions.transaction.Transaction entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.transactions.transaction.Transaction.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }
}
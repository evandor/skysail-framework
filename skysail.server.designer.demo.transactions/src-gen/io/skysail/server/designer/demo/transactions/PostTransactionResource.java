package io.skysail.server.designer.demo.transactions;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

/**
 * generated from postResource.stg
 */
public class PostTransactionResource extends PostEntityServerResource<io.skysail.server.designer.demo.transactions.Transaction> {

	protected TransactionsApplication app;

    public PostTransactionResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TransactionsApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.transactions.Transaction createEntityTemplate() {
        return new Transaction();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.transactions.Transaction entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.transactions.Transaction.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResource.class);
    }
}
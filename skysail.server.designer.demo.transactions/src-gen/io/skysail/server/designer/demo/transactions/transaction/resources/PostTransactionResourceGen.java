package io.skysail.server.designer.demo.transactions.transaction.resources;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.transactions.TransactionsApplication;
import io.skysail.server.designer.demo.transactions.transaction.Transaction;
import io.skysail.server.restlet.resources.PostEntityServerResource;

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
        OrientVertex saved = (OrientVertex) app.getRepository(io.skysail.server.designer.demo.transactions.transaction.Transaction.class).save(entity, app.getApplicationModel());
        entity.setId(saved.getIdentity().toString());

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TransactionsResourceGen.class);
    }
}
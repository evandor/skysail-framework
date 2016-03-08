package io.skysail.server.designer.demo.accounts.account.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.accounts.*;
import io.skysail.server.designer.demo.accounts.account.*;

/**
 * generated from postResource.stg
 */
public class PostAccountResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.accounts.account.Account> {

	protected AccountsApplication app;

    public PostAccountResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (AccountsApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.accounts.account.Account createEntityTemplate() {
        return new Account();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.accounts.account.Account entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.accounts.account.Account.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResourceGen.class);
    }
}
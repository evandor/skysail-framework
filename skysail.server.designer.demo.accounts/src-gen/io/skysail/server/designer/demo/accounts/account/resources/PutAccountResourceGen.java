package io.skysail.server.designer.demo.accounts.account.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.accounts.*;
import io.skysail.server.designer.demo.accounts.account.*;

/**
 * generated from putResource.stg
 */
public class PutAccountResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.accounts.account.Account> {


    protected String id;
    protected AccountsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (AccountsApplication)getApplication();
    }

    @Override
    public void updateEntity(Account  entity) {
        io.skysail.server.designer.demo.accounts.account.Account original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.accounts.account.Account.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.accounts.account.Account getEntity() {
        return (io.skysail.server.designer.demo.accounts.account.Account)app.getRepository(io.skysail.server.designer.demo.accounts.account.Account.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResourceGen.class);
    }
}
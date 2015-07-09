package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class PutAccountResource extends PutEntityServerResource<Account> {

    
    private MailApplication app;

    @Override
    protected void doInit() throws ResourceException {
        app = (MailApplication)getApplication();
    }
    
    @Override
    public Account getEntity() {
        return app.getRepository().findById(Account.class, getAttribute("id"));
    }

    @Override
    public SkysailResponse<?> updateEntity(Account entity) {
        Account original = getEntity(null);
        copyProperties(original,entity);
        app.getRepository().update(getAttribute("id"), original);
        return new SkysailResponse<>();
    }
    
}

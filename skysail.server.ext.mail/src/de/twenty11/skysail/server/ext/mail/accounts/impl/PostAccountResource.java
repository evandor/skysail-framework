package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class PostAccountResource extends PostEntityServerResource<Account> {

    private MailApplication app;

    @Override
    protected void doInit() throws ResourceException {
        app = (MailApplication) getApplication();
    }

    @Override
    public Account createEntityTemplate() {
        return new Account();
    }

    @Override
    public SkysailResponse<?> addEntity(Account entity) {
        app.getAccountsRepository().add(entity);
        return new SkysailResponse<>();
    }

}

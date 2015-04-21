package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class AccountResource extends EntityServerResource<Account> {

    private MailApplication app;
    private String accountId;

    public AccountResource() {
        app = (MailApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        accountId = (String) getRequest().getAttributes().get("id");
    }

    @Get("htmlform")
    public SkysailResponse<Account> createForm() {
        return new FormResponse<Account>(new Account(), ".");
    }

    public SkysailResponse<?> addEntity(Account entity) {
        app.getAccountRepository().add(entity);
        return new SkysailResponse<Account>();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account getEntity() {
        return app.getAccountRepository().getById(accountId);
    }

}

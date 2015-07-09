package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.links.Link;
import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.*;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;
import de.twenty11.skysail.server.ext.mail.folders.FoldersResource;

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
      //  app.getAccountsRepository().add(entity);
        return new SkysailResponse<Account>();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account getEntity() {
        return app.getRepository().findById(Account.class, accountId);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FoldersResource.class, PutAccountResource.class);
    }
}

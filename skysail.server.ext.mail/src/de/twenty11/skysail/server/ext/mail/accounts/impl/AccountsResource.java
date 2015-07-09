package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class AccountsResource extends ListServerResource<Account> {

    private MailApplication app;

    public AccountsResource() {
        super(AccountResource.class);
        app = (MailApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "List of accounts");
    }

    @Override
    public List<Account> getEntity() {
        return app.getRepository().findAll(Account.class);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostAccountResource.class);
    }

}

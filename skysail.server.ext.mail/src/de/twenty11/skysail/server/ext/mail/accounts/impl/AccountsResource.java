package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class AccountsResource extends ListServerResource<Account> {

    private MailApplication app;

    public AccountsResource() {
        super(AccountResource.class);
        app = (MailApplication) getApplication();
    }

    @Override
    public List<Account> getEntity() {
        return app.getAccountRepository().getAll();
    }

//    @Override
//    public List<Link> getLinks() {
//        List<Link> links = super.getLinks();
//        links.add(new RelativeLink(getContext(), "mail/accounts/?media=htmlform", "new Account"));
//        return links;
//    }

}

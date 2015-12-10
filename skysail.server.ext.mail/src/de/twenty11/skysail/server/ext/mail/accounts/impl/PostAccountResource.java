package de.twenty11.skysail.server.ext.mail.accounts.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;
import io.skysail.server.restlet.resources.PostEntityServerResource;

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
    public void addEntity(Account entity) {
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        app.getRepository().add(entity);
    }

}

package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
    public SkysailResponse<Account> addEntity(Account entity) {
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        app.getRepository().add(entity);
        return new SkysailResponse<>();
    }

}

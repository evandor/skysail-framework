package de.twenty11.skysail.server.ext.mail.folders;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;
import de.twenty11.skysail.server.ext.mail.mails.MailsResource;

public class FoldersResource extends ListServerResource<MailFolder> {

    private MailApplication app;
    private Account account;

    public FoldersResource() {
        super(FolderResource.class);
        app = (MailApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
       // super.doInit();
        if (getRequest().getAttributes().get("id") != null) {
            String accountId = (String) getRequest().getAttributes().get("id");
            account = null;//app.getAccountsRepository().getById(accountId);
        }
    }

    @Override
    public List<MailFolder> getEntity() {
        // Assuming pop3 for now, there is only one folder
        // http://stackoverflow.com/questions/5925944/how-to-retrieve-gmail-sub-folders-labels-using-pop3
        return Arrays.asList(new MailFolder("INBOX"));
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MailsResource.class);
    }

}

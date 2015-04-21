package de.twenty11.skysail.server.ext.mail.folders;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class FoldersResource extends ListServerResource<MailFolder> {

    private MailApplication app;
    private Account account;

    public FoldersResource() {
        super(null);
        app = (MailApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        if (getRequest().getAttributes().get("id") != null) {
            String accountId = (String) getRequest().getAttributes().get("id");
            account = app.getAccountRepository().getById(accountId);
        }
    }

    @Override
    public List<MailFolder> getEntity() {
        Validate.notNull(account);
        // Assuming pop3 for now, there is only one folder
        // http://stackoverflow.com/questions/5925944/how-to-retrieve-gmail-sub-folders-labels-using-pop3
        return Arrays.asList(new MailFolder("INBOX"));
    }

//    @Override
//    public List<Link> getLinks() {
//        List<Link> links = super.getLinks();
//        links.add(new RelativeLink("mail/accounts/" + account.getId() + "/folders/INBOX/mails", "open"));
//        return links;
//    }

}

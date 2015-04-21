package de.twenty11.skysail.server.ext.mail.mails;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;

import org.restlet.resource.ResourceException;

import com.sun.mail.pop3.POP3Store;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class MailsResource extends ListServerResource<Mail> {

    private MailApplication app;
    private String folderId;
    private Account account;

    public MailsResource() {
        super(null);
        app = (MailApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        String accountId = (String) getRequest().getAttributes().get("id");
        account = null;//app.getAccountsRepository().getById(accountId);
        folderId = (String) getRequest().getAttributes().get("folderId");
    }

    @Override
    public List<Mail> getEntity() {
        List<Mail> result = new ArrayList<Mail>();
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3s.host", account.getHost());
            Session emailSession = Session.getDefaultInstance(properties);

            // 2) create the POP3 store object and connect with the pop server
            POP3Store emailStore = (POP3Store) emailSession.getStore("pop3s");
            emailStore.connect(account.getUser(), account.getPass());

            // 3) create the folder object and open it
            Folder emailFolder = emailStore.getFolder(folderId);
            emailFolder.open(Folder.READ_ONLY);

            // 4) retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                result.add(new Mail(messages[i]));
            }

            // 5) close the store and folder objects
            emailFolder.close(false);
            emailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

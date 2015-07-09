package de.twenty11.skysail.server.ext.mail.mails;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.search.FlagTerm;

import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.ResourceException;

import com.sun.mail.pop3.POP3SSLStore;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

@Slf4j
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
        account = app.getRepository().findById(Account.class, accountId);
        folderId = (String) getRequest().getAttributes().get("folderId");
    }

    @Override
    public List<Mail> getEntity() {
        List<Mail> result = new ArrayList<Mail>();
        try {
//            Properties properties = new Properties();
//            properties.put("mail.pop3s.host", account.getHost());
////            properties.put("mail.pop3.port", account.getPort());
////            properties.put("mail.pop3.starttls.enable", true);
//            properties.put("mail.pop3.ssl.trust", "*");
//
//            
//            
//            
//            Session emailSession = Session.getDefaultInstance(properties);
//
//            // 2) create the POP3 store object and connect with the pop server
//            POP3Store emailStore = (POP3Store) emailSession.getStore("pop3s");
//            emailStore.connect(account.getUser().replace("&#64;", "@"), account.getPass());

            
            String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            Properties pop3Props = new Properties();

            pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
            pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
            pop3Props.setProperty("mail.pop3.port",  "995");
            pop3Props.setProperty("mail.pop3.socketFactory.port", "995");

            URLName url = new URLName("pop3", account.getHost(), 995, "",
                    account.getUser().replace("&#64;", "@"), account.getPass());

            Session session = Session.getInstance(pop3Props, null);
            session.setDebug(true);
            POP3SSLStore emailStore = new POP3SSLStore(session, url);
            emailStore.connect();
            
            // 3) create the folder object and open it
            Folder emailFolder = emailStore.getFolder(folderId);
            emailFolder.open(Folder.READ_ONLY);

//            // 4) retrieve the messages from the folder in an array and print it
//            Message[] messages = emailFolder.getMessages();
//            for (int i = 0; i < messages.length; i++) {
//                result.add(new Mail(messages[i]));
//            }
            
            
            Message messages[] = emailFolder.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));
            System.out.println("No. of Unread Messages : " + messages.length);

            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);

            fp.add(FetchProfile.Item.CONTENT_INFO);

            emailFolder.fetch(messages, fp);
            
            for (int i = 0; i < messages.length; i++) {
                log.info("reading mail {} of {}", i,messages.length+1);
                Mail mail = new Mail(getAttribute("id"), messages[i]);
                app.getRepository().add(mail);
                result.add(mail);
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

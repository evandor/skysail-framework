package de.twenty11.skysail.server.ext.mail;

import io.skysail.server.app.SkysailApplication;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.ext.mail.accounts.impl.AccountRepository;
import de.twenty11.skysail.server.ext.mail.accounts.impl.AccountResource;
import de.twenty11.skysail.server.ext.mail.accounts.impl.AccountsResource;
import de.twenty11.skysail.server.ext.mail.folders.FoldersResource;
import de.twenty11.skysail.server.ext.mail.mails.MailsResource;
import de.twenty11.skysail.server.ext.mail.mails.SendMailResource;

@Component(immediate = true)
public class MailApplication extends SkysailApplication implements ApplicationProvider {

	private static final String APP_NAME = "mail";
	private AccountRepository accountRepository;

	// private EntityManagerFactory enitityManagerFactory;

	public MailApplication() {
		super(APP_NAME);
	}

	// @Reference(target = "(osgi.unit.name=MailPU)")
	// public synchronized void setEntityManager(EntityManagerFactory emf) {
	// this.enitityManagerFactory = emf;
	// }

	@Override
	protected void attach() {
		router.attach(new RouteBuilder("", MailRootResource.class));
		router.attach(new RouteBuilder("/mail/", SendMailResource.class));
		router.attach(new RouteBuilder("/accounts", AccountsResource.class));
		router.attach(new RouteBuilder("/accounts/", AccountResource.class));
		router.attach(new RouteBuilder("/accounts/{id}", AccountResource.class));
		router.attach(new RouteBuilder("/accounts/{id}/folders", FoldersResource.class));
		router.attach(new RouteBuilder("/accounts/{id}/folders/{folderId}/mails", MailsResource.class));
	}

	public synchronized AccountRepository getAccountRepository() {
		if (accountRepository == null) {
			// accountRepository = new
			// EmfAccountRepository(enitityManagerFactory, this);
			accountRepository = null;//new SerializedAccountRepository();
		}
		return accountRepository;
	}

//	@Override
	public void send(String subject, String body) {
		send("to", subject, body);
	}

	public void send(String to, String subject, String body) {
		String from = "notes@evandor.de";

		String host = "XXX";//serverConfiguration.getConfigForKey(ServerConfiguration.EMAIL_HOST);

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.put("mail.debug", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.user", "carsten");
		properties.put("mail.smtp.password", "23");
		
//		props.put("mail.smtp.host", "SMTPHOST");
//		props.put("mail.smtp.port", "PORTNUMBER");
//		props.put("mail.transport.protocol","smtp");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.tls", "true");
//		props.put("mail.smtp.user", "EXAMPLENAME@PROVIDER.COM");
//		props.put("mail.password", "PASSWORD");
		
		Session session = Session.getDefaultInstance(properties);
		try {
			session.getTransport();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("evandor@gmail.com"));
			message.setSubject(subject);
			message.setText(body);

			//			Transport.send(message);
			
			Transport tr = session.getTransport("smtp");
			tr.connect(host, "", "");
			message.saveChanges();      // don't forget this
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		
	}

}

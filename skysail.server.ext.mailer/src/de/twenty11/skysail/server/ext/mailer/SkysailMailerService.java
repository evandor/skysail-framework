package de.twenty11.skysail.server.ext.mailer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.MailerService;

@Component(immediate = true, properties = { "service.pid=mail" })
@Slf4j
public class SkysailMailerService implements MailerService, ManagedService {

    private static final String MAIL_SERVICE_PID = "mail";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_USER = "mail.smtp.user";
    private static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";

    private Dictionary<String, ?> config;

    @Override
    public void send(String to, String subject, String body) {
        to = to.replace("&#64;", "@");
        String from = "notes@evandor.de";

        String host = "YYY";//serverConfiguration.getConfigForKey(ServerConfiguration.EMAIL_HOST);

        Properties properties = new Properties();
        set(properties, MAIL_SMTP_HOST);
        set(properties, MAIL_SMTP_USER);
        set(properties, MAIL_SMTP_PASSWORD);
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.auth", "true");

        // props.put("mail.smtp.port", "PORTNUMBER");
        // props.put("mail.transport.protocol","smtp");
        // props.put("mail.smtp.starttls.enable", "true");
        // props.put("mail.smtp.tls", "true");
        // props.put("mail.smtp.user", "EXAMPLENAME@PROVIDER.COM");
        // props.put("mail.password", "PASSWORD");

        log.info("about to send new mail with subject '{}' to '{}'", subject, to);
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication((String) config.get(MAIL_SMTP_USER), (String) config
                        .get(MAIL_SMTP_PASSWORD));
            }
        });

        try {
            session.getTransport();
        } catch (NoSuchProviderException e) {
            log.error("NoSuchProvider", e);
        }

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            // Transport.send(message);

            Transport tr = session.getTransport("smtp");
            tr.connect(host, "", "");
            message.saveChanges(); // don't forget this
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

        } catch (MessagingException mex) {
            log.error(mex.getMessage(), mex);
        }
    }

    private void set(Properties properties, String key) {
        properties.setProperty(key, (String) config.get(key));
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties == null) {
            this.config = getDefaults();
            createConfiguration();
            return;
        }
        this.config = properties;
    }

    private void createConfiguration() {
        String fileInstallDirPropertyName = "felix.fileinstall.dir";
        String fileinstallDir = System.getProperty(fileInstallDirPropertyName);
        if (fileinstallDir == null) {
            log.warn("could not determine system property {}", fileInstallDirPropertyName);
            return;
        }
        StringBuilder content = new StringBuilder();
        content.append("###################################################################################\n");
        content.append("automatically created mail configuration: please update accordingly                \n");
        content.append("###################################################################################\n\n");
        
        for (String key : getDefaults().keySet()) {
            content.append(key).append("=").append(getDefaults().get(key)).append("\n");
        }
        String configFile = fileinstallDir + File.separator + MAIL_SERVICE_PID +".cfg";
        try {
            Files.write(Paths.get(configFile), content.toString().getBytes());
        } catch (IOException e1) {
            log.warn("not able to write to configuration file " + configFile + "'", e1);
        }
    }

    private Hashtable<String, String> getDefaults() {
        Hashtable<String, String> defaults = new Hashtable<>();
        defaults.put("mail.smtp.host", "mail.xxx.com");
        defaults.put("mail.smtp.user", "xxx");
        defaults.put("mail.smtp.password", "xxx");
        defaults.put(Constants.SERVICE_PID, MAIL_SERVICE_PID);
        return defaults;
    }

}

package de.twenty11.skysail.server.ext.mail.mails;

import javax.mail.Message;

public class Mail {

    private String subject;
    private Object content;

    public Mail(Message message) {
        try {
            this.subject = message.getSubject();
            this.content = message.getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSubject() {
        return subject;
    }

    public Object getContent() {
        return "<pre>" + this.content.toString() + "</pre>";
    }

}

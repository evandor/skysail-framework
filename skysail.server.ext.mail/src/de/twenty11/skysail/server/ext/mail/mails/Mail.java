package de.twenty11.skysail.server.ext.mail.mails;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.util.Date;

import javax.mail.Message;

import lombok.*;

import com.sun.mail.pop3.POP3Message;

@Getter
@Setter
public class Mail implements Identifiable {

    @Field
    private String messageId;

    @Field
    private String subject;

    @Field
    private String contentType;

    @Field
    private Date sentDate;

    @Field
    private int size;

    @Field
    private String messageCls;

    @Field
    private String accountId;

//    @Field
//    private List<String> from;

   // private Object content;

    public Mail(String accountId, Message message) {
        try {
            this.accountId = accountId;
            size = message.getSize();
            this.subject = message.getSubject();
            contentType = message.getContentType();
            sentDate = message.getSentDate();
            messageCls = message.getClass().getSimpleName();
            if (message instanceof POP3Message) {
                this.messageId = ((POP3Message)message).getMessageID();
            }
            //this.from = Arrays.stream(message.getFrom()).map(f -> f.toString()).collect(Collectors.toList());
     //       this.content = message.getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }



}

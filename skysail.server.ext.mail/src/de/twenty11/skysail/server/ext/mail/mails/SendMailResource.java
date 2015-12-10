package de.twenty11.skysail.server.ext.mail.mails;

import org.restlet.data.Form;
import org.restlet.resource.Get;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SendMailResource extends EntityServerResource<Message>{

	private MailApplication app;

	public SendMailResource() {
		app = (MailApplication)getApplication();
	}
	
	@Get("htmlform")
    public SkysailResponse<Message> createForm() {
        return new FormResponse(getResponse(), new Message(),".");
    }

    public Message getData(Form form) {
        Message message = new Message();
        message.setSubject(form.getFirstValue("subject"));
        message.setTo(form.getFirstValue("to"));
        message.setBody(form.getFirstValue("body"));
        return message;
    }
    
    public SkysailResponse<?> addEntity(Message message) {
    	app.send(message.getTo(), message.getSubject(), message.getBody());
    	return new SkysailResponse<String>();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message getEntity() {
        return null;
    }

}

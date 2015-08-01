package de.twenty11.skysail.server.ext.mail.mails;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;

import javax.validation.constraints.NotNull;

public class Message {
	
	@Field
	private String subject;
	
	@Field
	@NotNull
	private String to;
	
	@Field(inputType=InputType.TEXTAREA)
	private String body;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}

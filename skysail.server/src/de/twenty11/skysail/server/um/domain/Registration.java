package de.twenty11.skysail.server.um.domain;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.*;

import io.skysail.domain.html.*;
import lombok.*;

@PasswordsMatch
@Getter
@Setter
public class Registration {

	public enum RegistrationStatus {
	    NEW, CONFIRMED
    }
	
	@Id
	private Object rid;

	@NotNull(message = "Please provide a valid email address as your username.")
	//http://www.regular-expressions.info/index.html, @ is html escaped: &#64;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+&#64;[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "username must be an email address")
	@Field(inputType = InputType.EMAIL)
	private String username;

	@NotNull(message = "The Password must not be empty")
    @Size(min = 6, message = "Sorry, the password must have at least 6 Characters.")
	@Field(inputType = InputType.PASSWORD, htmlPolicy = HtmlPolicy.NO_HTML)
	private String password;

	@Field(inputType = InputType.PASSWORD)
	@Transient
	private String pwdRepeated;

    @Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmationDate;
    
    private String confirmationUrl;
    
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

	public Registration(String username, String password, String pwdRepeated, String confirmationUrl) {
		this.username = username;
		this.password = password;
		this.pwdRepeated = pwdRepeated;
		this.registrationDate = new Date();
		this.status = RegistrationStatus.NEW;
		this.confirmationUrl = confirmationUrl;
	}

	public Registration() {
	}

}

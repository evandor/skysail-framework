package de.twenty11.skysail.server.app.profile;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.HtmlPolicy;
import de.twenty11.skysail.api.forms.InputType;

/**
 * TODO: make sure to (programmatically) apply the same password validation rules like in skysailUser and registrations
 *
 */
@Data
@PasswordsMatch
public class ChangePasswordEntity {

	@Field(type = InputType.READONLY)
	private String account;

    @Field(type = InputType.PASSWORD, htmlPolicy = HtmlPolicy.NO_HTML)
	private String old;

	@NotNull(message = "The Password must not be empty")
    @Size(min = 6, message = "Sorry, the password must have at least 6 Characters.")
	@Field(type = InputType.PASSWORD, htmlPolicy = HtmlPolicy.NO_HTML)
	private String password;

	@Field(type = InputType.PASSWORD)
	@Transient
	private String pwdRepeated;

}

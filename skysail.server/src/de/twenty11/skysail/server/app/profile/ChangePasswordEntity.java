package de.twenty11.skysail.server.app.profile;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.HtmlPolicy;
import io.skysail.api.forms.InputType;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * TODO: make sure to (programmatically) apply the same password validation rules like in skysailUser and registrations
 *
 */
@Data
@PasswordsMatch
public class ChangePasswordEntity {

	@Field(inputType = InputType.READONLY)
	private String account;

    @Field(inputType = InputType.PASSWORD, htmlPolicy = HtmlPolicy.NO_HTML)
	private String old;

	@NotNull(message = "The Password must not be empty")
    @Size(min = 6, message = "Sorry, the password must have at least 6 Characters.")
	@Field(inputType = InputType.PASSWORD, htmlPolicy = HtmlPolicy.NO_HTML)
	private String password;

	@Field(inputType = InputType.PASSWORD)
	@Transient
	private String pwdRepeated;

}

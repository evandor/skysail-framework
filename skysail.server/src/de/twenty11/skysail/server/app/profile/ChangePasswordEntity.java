package de.twenty11.skysail.server.app.profile;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;

import javax.persistence.Transient;
import javax.validation.constraints.*;

import lombok.Data;

/**
 * TODO: make sure to (programmatically) apply the same password validation rules like in skysailUser and registrations
 *
 */
@Data // NO_UCD
@PasswordsMatch
public class ChangePasswordEntity implements Identifiable {

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

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}

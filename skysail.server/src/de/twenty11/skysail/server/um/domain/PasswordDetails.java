package de.twenty11.skysail.server.um.domain;

import io.skysail.api.forms.Field;

import javax.validation.constraints.Size;

public class PasswordDetails {

	@Field
	private String oldPassword;

	@Field
	@Size(min=8, message="PASSWORD_AT_LEAST_8_CHARS")
	private String newPassword1;

	@Field//(type = InputType.PASSWORD)
	private String newPassword2;

	public PasswordDetails(String old, String newPassword,
			String newPasswordRepeated) {
		oldPassword = old;
		newPassword1 = newPassword;
		newPassword2 = newPasswordRepeated;
	}

	public PasswordDetails() {
		// TODO Auto-generated constructor stub
	}

	public String getOldPassword() {
		return oldPassword;
	}
	
	public String getNewPassword1() {
		return newPassword1;
	}
	
	public String getNewPassword2() {
		return newPassword2;
	}
	
}

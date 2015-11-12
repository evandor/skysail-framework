package io.skysail.server.codegen.test.crm.contacts;

import io.skysail.api.forms.Field;

import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
public class Contact {

	@Field
	@Size(min = 2)
	@NotNull
	private String name;

	@Field
	@Size(min = 2)
	private String surname;

}

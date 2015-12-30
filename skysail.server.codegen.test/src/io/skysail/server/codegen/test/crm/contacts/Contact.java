package io.skysail.server.codegen.test.crm.contacts;

import javax.validation.constraints.*;

import io.skysail.domain.html.Field;
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

package io.skysail.server.ext.apt.test.crm.contacts;

import io.skysail.api.forms.Field;

import javax.validation.constraints.*;

import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

@GeneratePostResource
@GenerateListResource(linkheader = "PostContactResource")
@GeneratePutResource
@GenerateEntityResource(linkheader = "PutContactResource")
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

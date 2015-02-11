package io.skysail.server.ext.apt.test.crm.contacts;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;

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

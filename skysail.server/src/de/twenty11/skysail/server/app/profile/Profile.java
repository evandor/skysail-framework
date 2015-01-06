package de.twenty11.skysail.server.app.profile;

import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import lombok.Data;
import lombok.NonNull;

@Data
public class Profile {
	
	@Field(type = InputType.READONLY)
	@NonNull
	private String accountname;
	
	@Field
	private String name;
	
	@Field
	private String surname;

}

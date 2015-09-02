package de.twenty11.skysail.server.app.profile;

import io.skysail.api.forms.*;
import lombok.*;

@Data// NO_UCD
public class Profile {

	@Field(inputType = InputType.READONLY)
	@NonNull
	private String accountname;

	@Field
	private String name;

	@Field
	private String surname;

}

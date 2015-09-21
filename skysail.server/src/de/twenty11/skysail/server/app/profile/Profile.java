package de.twenty11.skysail.server.app.profile;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import lombok.*;

@Data// NO_UCD
public class Profile implements Identifiable {

	@Field(inputType = InputType.READONLY)
	@NonNull
	private String accountname;

	@Field
	private String name;

	@Field
	private String surname;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}

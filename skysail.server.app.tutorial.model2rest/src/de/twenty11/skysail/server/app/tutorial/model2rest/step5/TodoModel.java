package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import lombok.Data;

@Data
public class TodoModel implements Identifiable {

	@Id
	private int id;

	@Field
	@Size(min = 3)
	private String todo;

	@Field(inputType = InputType.TEXTAREA)
	@Size(max = 1000)
	private String desc;

	// @Field
	// private Date due;

	private State state;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}

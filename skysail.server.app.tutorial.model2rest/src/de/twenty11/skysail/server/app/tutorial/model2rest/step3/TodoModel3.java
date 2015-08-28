package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import io.skysail.api.forms.Field;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TodoModel3 {

    @Id
    private int id;

    @Field
    @Size(min = 3)
    private String todo;

}

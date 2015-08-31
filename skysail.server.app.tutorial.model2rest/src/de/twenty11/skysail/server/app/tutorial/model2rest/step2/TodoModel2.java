package de.twenty11.skysail.server.app.tutorial.model2rest.step2;

import io.skysail.api.forms.Field;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TodoModel2 {

    @Field
    @Size(min = 3)
    private String todo;

}

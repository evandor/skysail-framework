package de.twenty11.skysail.server.app.tutorial.model2rest.step1;

import io.skysail.api.forms.Field;
import lombok.Data;

@Data
public class TodoModel1 {

    @Field
    private String todo;

}

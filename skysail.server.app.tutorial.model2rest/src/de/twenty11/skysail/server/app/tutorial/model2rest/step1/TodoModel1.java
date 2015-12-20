package de.twenty11.skysail.server.app.tutorial.model2rest.step1;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import lombok.Data;

@Data
public class TodoModel1 implements Identifiable {

    @Field
    private String todo;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}

package de.twenty11.skysail.server.app.tutorial.model2rest.step1;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
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

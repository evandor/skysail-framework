package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TodoModel3 implements Identifiable {

    @Id
    private int id;

    @Field
    @Size(min = 3)
    private String todo;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }


}

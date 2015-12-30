package de.twenty11.skysail.server.app.tutorial.model2rest.step4;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import lombok.Data;

@Data
public class TodoModel4 implements Identifiable{

    @Id
    private int id;

    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML, inputType = InputType.TEXTAREA)
    private String todo;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}

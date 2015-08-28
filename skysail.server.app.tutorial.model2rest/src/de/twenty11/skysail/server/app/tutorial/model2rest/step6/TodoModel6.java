package de.twenty11.skysail.server.app.tutorial.model2rest.step6;

import io.skysail.api.forms.*;

import javax.persistence.Id;

import lombok.Data;

@Data
public class TodoModel6 {

    @Id
    private int id;

    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML, inputType = InputType.TEXTAREA)
    private String todo;

}

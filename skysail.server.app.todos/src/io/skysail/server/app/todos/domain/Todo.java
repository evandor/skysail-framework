package io.skysail.server.app.todos.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;

@Getter
@Setter
@ToString(of = { "title" })
@JsonIgnoreProperties({ "handler" })
public class Todo {

    @Field
    @Size(min = 2)
    private String title;

    @Field(type = InputType.TEXTAREA)
    private String desc;

    @Field(type = InputType.DATE)
    private Date due;

    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY)
    private String owner;

}

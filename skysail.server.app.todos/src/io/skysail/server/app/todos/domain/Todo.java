package io.skysail.server.app.todos.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Getter
@Setter
@ToString(of = { "title" })
// @JsonIgnoreProperties({ "handler" })
public class Todo {

    @Field(listView = ListView.TRUNCATE)
    @Size(min = 2)
    private String title;

    @Field(type = InputType.TEXTAREA, listView = ListView.TRUNCATE)
    private String desc;

    @Field(type = InputType.DATE)
    private Date due;

    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    private String owner;

    // start date, due date, Priority (high, medium, low),
    // status: not started, in progress, completed. Pending inßut, deferred
    // assigned to,
    // related to: accont, ...
}

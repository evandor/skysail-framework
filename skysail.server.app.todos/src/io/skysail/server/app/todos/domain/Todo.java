package io.skysail.server.app.todos.domain;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.ListView;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Setter
@ToString(of = { "title" })
@JsonPropertyOrder({ "title", "desc" })
public class Todo implements Serializable {

    private static final long serialVersionUID = -6320289870876900108L;

    @Id
    private String id;

    @Field(listView = { ListView.TRUNCATE, ListView.LINK })
    @NotNull
    @Size(min = 2)
    private String title;

    @Field(type = InputType.TEXTAREA, listView = { ListView.TRUNCATE })
    private String desc;

    @Field(type = InputType.DATE)
    private Date due;

    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY, listView = { ListView.HIDE })
    private String owner;

    // start date, due date, Priority (high, medium, low),
    // status: not started, in progress, completed. Pending inßut, deferred
    // assigned to,
    // related to: accont, ...
}

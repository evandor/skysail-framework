package io.skysail.server.app.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.ListView;
import io.skysail.server.app.todos.lists.UniquePerOwner;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Setter
@ToString(of = { "id", "name" })
@NoArgsConstructor
@JsonPropertyOrder({ "title", "desc" })
public class TodoList implements Serializable, Identifiable {

    public TodoList(String name) {
        this.name = name;
    }

    private static final long serialVersionUID = -3188923584006747102L;

    @Id
    private String id;

    @Field(listView = { ListView.TRUNCATE, ListView.LINK })
    @NotNull
    @Size(min = 2)
    @UniquePerOwner
    private String name;

    @Field(type = InputType.TEXTAREA, listView = { ListView.TRUNCATE })
    private String desc;

    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY, listView = { ListView.HIDE })
    private String owner;

}
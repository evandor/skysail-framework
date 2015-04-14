package io.skysail.server.app.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.Postfix;
import io.skysail.server.app.todos.lists.UniquePerOwner;
import io.skysail.server.app.todos.todos.resources.TodosResource;
import io.skysail.server.forms.ListView;

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
@UniquePerOwner
public class TodoList implements Serializable, Identifiable {

    private static final long serialVersionUID = -3188923584006747102L;

    public TodoList(String name) {
        this.name = name;
    }

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    @Postfix(methodName = "todosCount")
    @ListView(link = TodosResource.class, truncate = 20)
    private String name;

    @Field(type = InputType.TEXTAREA)//, listView = { ListViewEnum.TRUNCATE })
    private String desc;

    @Field(type=InputType.READONLY)//, listView = { ListViewEnum.HIDE})
    @ListView(hide = true)
    private Long todosCount;
    
    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY)//, listView = { ListViewEnum.HIDE })
    @ListView(hide = true)
    private String owner;

    public long todosCount() {
        return todosCount;
    }
}
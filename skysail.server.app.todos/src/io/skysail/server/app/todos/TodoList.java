package io.skysail.server.app.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.todos.lists.UniquePerOwner;
import io.skysail.server.app.todos.todos.resources.TodosResource;
import io.skysail.server.forms.ListView;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

@Getter
@Setter
@ToString(of = { "id", "name" })
@NoArgsConstructor
@JsonPropertyOrder({ "title", "desc" })
@UniquePerOwner
@JsonIgnoreProperties("handler")
public class TodoList implements Serializable, Identifiable {

    private static final long serialVersionUID = -3188923584006747102L;

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    @ListView(link = TodosResource.class, truncate = 20)
    private String name;

    @Field(inputType=InputType.READONLY)
    private Long todosCount;

    @Field(inputType = InputType.TEXTAREA)
    private String desc;

    @Field(inputType = InputType.READONLY)
    private Date created;

    @Field(inputType = InputType.READONLY)
    private Date modified;

    @Field(inputType = InputType.CHECKBOX)
    private boolean defaultList;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    public long todosCount() { // NO_UCD
        return todosCount;
    }

    public TodoList(String name) {
        this.name = name;
    }

}
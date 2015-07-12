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
    
    @Field(type=InputType.READONLY)
    private Long todosCount;

    @Field(type = InputType.TEXTAREA)
    private String desc;

    
    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    public long todosCount() {
        return todosCount;
    }

    public TodoList(String name) {
        this.name = name;
    }
    
}
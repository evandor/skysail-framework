package io.skysail.server.app.todos.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.app.todos.todos.status.*;
import io.skysail.server.forms.*;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

import org.restlet.data.Form;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Setter
@ToString(of = { "title" })
@JsonPropertyOrder({ "title", "desc" })
@NoArgsConstructor
public class Todo implements Serializable, Identifiable {

    private static final long serialVersionUID = -6320289870876900108L;

    @Id
    private String id;
    
    @Reference(cls = Todo.class, selectionProvider = ListSelectionProvider.class)
    @PostView(visibility = Visibility.SHOW_IF_NULL)
    @ListView(hide = true)
    private String list;

    @Field
    @ListView(truncate = 20, link = PutTodoResource.class)
    @NotNull
    @Size(min = 2)
    private String title;

    @Field(type = InputType.TEXTAREA)
    @ListView(truncate = 10)
    private String desc;

    @Field(type = InputType.DATE)
    private Date due;

    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY)
    private Integer rank;

    @Field(selectionProvider = StatusSelectionProvider.class)
    @PostView(visibility = Visibility.HIDE)
    private Status status;

    @Field(type = InputType.READONLY)
    @ListView(hide=true)
    private String owner;

    // start date, 
    // assigned to,
    // related to: account, ...
    
    public Todo(String title) {
        this.title = title;
    }
    
    public Todo(Form query) {
        if (query == null) {
            return;
        }
        this.title = query.getFirstValue("title");
        this.desc = query.getFirstValue("desc");
    }

}

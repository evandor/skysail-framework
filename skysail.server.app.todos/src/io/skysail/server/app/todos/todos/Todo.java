package io.skysail.server.app.todos.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.Reference;
import io.skysail.server.app.todos.todos.resources.TodoResource;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.app.todos.todos.status.StatusSelectionProvider;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    
    @Reference(cls = Todo.class)//, postView = {PostView.HIDE})
    private String list;

    @Field
    @ListView(truncate = 20, link = TodoResource.class)
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
    @PostView(hide = true)
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
        this.title = query.getFirstValue("title");
        this.desc = query.getFirstValue("desc");
    }

}

package io.skysail.server.app.todos.todos;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.ListView;
import io.skysail.api.forms.PostView;
import io.skysail.api.forms.Reference;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.app.todos.todos.status.StatusSelectionProvider;

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

import de.twenty11.skysail.api.domain.Identifiable;

@Getter
@Setter
@ToString(of = { "title" })
@JsonPropertyOrder({ "title", "desc" })
@NoArgsConstructor
public class Todo implements Serializable, Identifiable {

    private static final long serialVersionUID = -6320289870876900108L;

    @Id
    private String id;
    
    @Reference(cls = Todo.class, postView = {PostView.HIDE})
    private String list;

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

    @Field(type = InputType.READONLY)
    private Integer rank;

    @Field(selectionProvider = StatusSelectionProvider.class, postView = { PostView.HIDE })
    private Status status;

    @Field(type = InputType.READONLY, listView = { ListView.HIDE })
    private String owner;

    // start date, 
    // status: 
    // assigned to,
    // related to: accont, ...
    
    public Todo(String title) {
        this.title = title;
    }
}

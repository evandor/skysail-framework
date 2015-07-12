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

@Getter
@Setter
@ToString(of = { "title" })
@NoArgsConstructor
@StartDateBeforeDueDate
public class TodoSummary implements Serializable, Identifiable {

    private static final long serialVersionUID = -6320289870876900108L;

    @Id
    private String id;
    
    @Field
    @ListView(truncate = 30, link = PutTodoResource.class)
    @NotNull
    @Size(min = 2)
    private String title;
    
    @Reference(cls = TodoSummary.class, selectionProvider = ListSelectionProvider.class)
    @ValidListId
    private String list;
    
    public String getList() {
        return "<a href='/Todos/Lists/"+list+"/'>" +  list +"</a>";
    }
    
    @Field(type = InputType.DATE)
    private Date due;

    @Field(type = InputType.DATE)
    private Date startDate;
    

    @Field(selectionProvider = StatusSelectionProvider.class)
    @PostView(visibility = Visibility.HIDE)
    @Submit
    private Status status;

    @Field(type = InputType.READONLY)
    private Integer views;

    public TodoSummary(Todo todo) {
        this.id = todo.getId();
        this.due = todo.getDue();
        this.list = todo.getList();
        this.startDate = todo.getStartDate();
        this.status = todo.getStatus();
        this.title = todo.getTitle();
        this.views = todo.getViews();
    }

}

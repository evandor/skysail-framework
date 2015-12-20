package io.skysail.server.app.todos.todos;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.app.todos.todos.status.*;
import io.skysail.server.forms.*;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@ToString(of = { "title" })
@NoArgsConstructor
@StartDateBeforeDueDate
public class TodoSummary implements Identifiable {

    @Id
    private String id;

    @Field
    @ListView(truncate = 30, link = PutTodoResource.class)//, prefix="status")
    @NotNull
    @Size(min = 2)
    private String title;

    @Reference(selectionProvider = ListSelectionProvider.class)
    @ValidListId
    private String list;

    public String getList() {
        return "<a href='/Todos/Lists/"+list+"/'>" +  list +"</a>";
    }

    @Field(inputType = InputType.DATE)
    private Date due;

    @Field(inputType = InputType.DATE)
    private Date startDate;


    @Field(selectionProvider = StatusSelectionProvider.class)
    @PostView(visibility = Visibility.HIDE)
    @ListView(colorize = "color")
    @Submit
    private Status status;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private Integer views;

    public TodoSummary(Todo todo) {
        this.id = todo.getId();
        this.due = todo.getDue();
        this.startDate = todo.getStartDate();
        this.status = todo.getStatus();
        this.title = todo.getTitle();
        this.views = todo.getViews();
    }

}

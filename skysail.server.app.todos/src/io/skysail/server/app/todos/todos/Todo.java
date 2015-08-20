package io.skysail.server.app.todos.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.app.todos.todos.status.*;
import io.skysail.server.forms.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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
@StartDateBeforeDueDate
public class Todo implements Serializable, Identifiable {

    private static final long serialVersionUID = -6320289870876900108L;

    @Id
    private String id;

    @Reference(selectionProvider = ListSelectionProvider.class)
    @PostView(visibility = Visibility.SHOW_IF_NULL)
    @ListView(hide = true)
    @ValidListId
    private String parent;

    @Field
    @ListView(truncate = 30, link = PutTodoResource.class, prefix = "status")
    @NotNull
    @Size(min = 2)
    //@Prefix(methodName = "status")
    private String title;

    @Field(inputType = InputType.TEXTAREA)
    @ListView(truncate = 20)
    private String desc;

    @Field(inputType = InputType.DATE)
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date due;

    @Field(inputType = InputType.DATE)
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd.MM.yyyy")
    private Date startDate;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private Integer elapseTime = 3;

    @Field(inputType = InputType.READONLY)
    private Date created;

    @Field(inputType = InputType.READONLY)
    private Date modified;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private Integer rank;

    @Field(inputType = InputType.RANGE)
    @Min(0)
    @Max(100)
    @ListView(hide=true)
    private Integer importance;

    @Field(inputType = InputType.READONLY)
    @ListView(hide=true)
    private Integer urgency;

    @Field(inputType = InputType.READONLY)
    @ListView(hide=true)
    private String owner;

    @Field(inputType = InputType.READONLY)
    @ListView(hide=true)
    private Integer views;

    @Field(selectionProvider = StatusSelectionProvider.class)
    @PostView(visibility = Visibility.HIDE)
    @ListView(colorize = "color")
    @Submit
    private Status status;

    // assigned to,
    // related to: account, ...

    public Todo(String title) {
        this.title = title;
    }

    public Todo(Form query, String listId, Locale locale) {
        if (query == null) {
            return;
        }
        // to add new entity via get URL
        this.title = query.getFirstValue("title");
        this.desc = query.getFirstValue("desc");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
        try {
            String dueDate = query.getFirstValue("due");
//            DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.FULL, locale);
//            this.due = dateInstance.parse(dueDate);
            this.due = sdf.parse(dueDate);
        } catch (Exception e) {
            // ignore
        }
        this.parent = listId;
        if (listId == null) {
            this.parent = query.getFirstValue("list");
        }
    }

    public Integer getElapseTime() {
        if (due != null && startDate != null) {
            return (int)((due.getTime() - startDate.getTime()) / (1000*60*60*24l));
        }
        return null;
    }

}

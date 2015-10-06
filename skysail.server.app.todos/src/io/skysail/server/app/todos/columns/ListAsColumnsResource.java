package io.skysail.server.app.todos.columns;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.*;

import java.util.*;

import org.apache.shiro.SecurityUtils;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListAsColumnsResource extends ListServerResource<TodoColumn> {

    private TodoApplication app;

    public ListAsColumnsResource() {
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
        addToContext(ResourceContextId.LINK_TITLE, "Show By State");
        app = (TodoApplication)getApplication();
    }

    @Override
    public List<TodoColumn> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());

        Map<Status, TodoColumn> map = new LinkedHashMap<>();
        addTodoColumn(map, Status.NEW);
        addTodoColumn(map, Status.WIP);
        addTodoColumn(map, Status.DONE);


        app.getTodosRepo().findAllTodos(filter).stream().forEach(todo -> {
            TodoColumn todoColumn = map.get(todo.getStatus());
            if (todoColumn != null) {
                todoColumn.add(todo);
            }
        });
        return new ArrayList<TodoColumn>(map.values());
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        //links.add(PostListResource.class);
        return super.getLinks(links);
    }


    private void addTodoColumn(Map<Status, TodoColumn> map, Status status) {
        map.put(status, new TodoColumn(status));
    }


}

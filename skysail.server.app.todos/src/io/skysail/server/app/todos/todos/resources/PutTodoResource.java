package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.*;

import org.restlet.resource.ResourceException;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication) getApplication();
    }

    @Override
    public Todo getEntity() {
         Todo todo = app.getRepository().getById(Todo.class, getAttribute(TodoApplication.TODO_ID));
         todo.setViews(todo.getViews() != null ? 1 + todo.getViews() : 1);
         app.getRepository().update(todo.getId(), todo, "parent");
         return todo;
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        Todo original = getEntity(null);
        copyProperties(original,entity);
        original.setModified(new Date());
        original.setUrgency(Ranker.calcUrgency(original));
        //original.setParent(null);
        Integer views = original.getViews();
        if (views == null) {
            original.setViews(1);
        }
        app.getRepository().update(getAttribute(TodoApplication.LIST_ID), original, "parent");
        return new SkysailResponse<>();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class);
    }

    @Override
    public String redirectTo() {
       // return super.redirectTo(TodosResource.class);
        return super.redirectTo(ListsResource.class);
    }

}

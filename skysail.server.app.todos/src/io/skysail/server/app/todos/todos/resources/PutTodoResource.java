package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.*;

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
         Todo todo = app.getTodosRepo().findOne(getAttribute(TodoApplication.TODO_ID));
         todo.setViews(todo.getViews() != null ? 1 + todo.getViews() : 1);
         app.getTodosRepo().update(todo.getId(), todo, "todos");
         return todo;
    }

    @Override
    public SkysailResponse<Todo> updateEntity(Todo entityFromTheWire) {

        TodoList list = app.getListRepo().findOne(entityFromTheWire.getParent());

        Todo entityToBeUpdated = getEntity(null);
        copyProperties(entityToBeUpdated,entityFromTheWire);
        entityToBeUpdated.setModified(new Date());
        entityToBeUpdated.setUrgency(Ranker.calcUrgency(entityToBeUpdated));
        Integer views = entityToBeUpdated.getViews();
        if (views == null) {
            entityToBeUpdated.setViews(1);
        }
        //app.getTodosRepo().update(getAttribute(TodoApplication.LIST_ID), entityToBeUpdated, "todos");

        //list.getTodos().stream().map(todo -> todo.getId()).filter(id -> id.equals(entityToBeUpdated.getId())).findFirst().ifPresent(list.getTodos());

        app.getListRepo().update(list.getId(), list, "todos");

        return new SkysailResponse<>(entityToBeUpdated);
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        return super.getLinks(links);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}

package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
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
         Todo todo = app.getTodosRepo().getById(getAttribute(TodoApplication.TODO_ID));
         todo.setViews(todo.getViews() != null ? 1 + todo.getViews() : 1);
         app.getTodosRepo().update(todo.getId(), todo, "parent");
         return todo;
    }

    @SuppressWarnings("deprecation")
    @Override
    public SkysailResponse<Todo> updateEntity(Todo entityFromTheWire) {
        Todo entityToBeUpdated = getEntity(null);
        copyProperties(entityToBeUpdated,entityFromTheWire);
        entityToBeUpdated.setModified(new Date());
        entityToBeUpdated.setUrgency(Ranker.calcUrgency(entityToBeUpdated));
        //original.setParent(null);
        Integer views = entityToBeUpdated.getViews();
        if (views == null) {
            entityToBeUpdated.setViews(1);
        }
        if (Status.PLANNED.equals(entityToBeUpdated.getStatus())) {
            entityToBeUpdated.setStatus(Status.NEW);
        } else if (Status.FINISHED.equals(entityToBeUpdated.getStatus())) {
            entityToBeUpdated.setStatus(Status.DONE);
        } else if (Status.ARCHIVED.equals(entityToBeUpdated.getStatus())) {
            entityToBeUpdated.setStatus(Status.DONE);
        }

        app.getTodosRepo().update(getAttribute(TodoApplication.LIST_ID), entityToBeUpdated, "parent");
        return new SkysailResponse<>(entityToBeUpdated);
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        return super.getLinks(links);
    }

    @Override
    public String redirectTo() {
       // return super.redirectTo(TodosResource.class);
        return super.redirectTo(ListsResource.class);
    }

}

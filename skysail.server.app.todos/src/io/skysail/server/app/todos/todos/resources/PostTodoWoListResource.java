package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;

import java.util.Date;
import java.util.function.Consumer;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoWoListResource extends PostTodoResource {

    private TodoApplication app;
    private String listId;

    public PostTodoWoListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
    protected void doInit() {
        app = (TodoApplication) getApplication();
        listId = getAttribute(TodoApplication.LIST_ID);
    }

    @Override
    public Todo createEntityTemplate() {
        return new Todo(getQuery());
    }

    @Override
    public SkysailResponse<?> addEntity(Todo entity) {
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setStatus(Status.NEW);
        entity.setRank(1);
        entity.setList(listId);
        String id = app.getRepository().add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        if ("submitAndNew".equals(submitValue)) {
            return super.redirectTo(PostTodoWoListResource.class);
        }
        return super.redirectTo(TodosResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            l.substitute(TodoApplication.LIST_ID, listId);
        };
    }
}
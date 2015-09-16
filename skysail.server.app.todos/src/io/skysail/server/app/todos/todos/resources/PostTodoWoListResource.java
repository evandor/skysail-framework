package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class PostTodoWoListResource extends PostTodoResource {

    private String listIdFromEntity;

    @Override
    public SkysailResponse<Todo> addEntity(Todo entity) {
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setStatus(Status.NEW);
        entity.setRank(1);
        listIdFromEntity = entity.getParent().replace("#","");
        entity.setParent(listIdFromEntity);
        //String id = app.getRepository().add(entity).toString();
        String id = app.getTodosRepo().save(entity, "parent").toString();
        entity.setId(id);
        return new SkysailResponse<>();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class);
    }

    @Override
    public String redirectTo() {
        //getRequest().getAttributes().put(TodoApplication.LIST_ID, listIdFromEntity);
        getContext().getAttributes().put(TodoApplication.LIST_ID, listIdFromEntity);
        if ("submitAndNew".equals(submitValue)) {
            return super.redirectTo(PostTodoWoListResource.class);
        }
        return super.redirectTo(Top10TodosResource.class);
    }

}
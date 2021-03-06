package io.skysail.server.app.todos.todos.resources;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class PostTodoWoListResource extends PostTodoResource {

    private String listIdFromEntity;

    @Override
    public void addEntity(Todo entity) {
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setStatus(Status.NEW);
        entity.setRank(1);
//        listIdFromEntity = entity.getParent().replace("#","");
//        entity.setParent(listIdFromEntity);
        //String id = app.getRepository().add(entity).toString();
        String id = app.getTodosRepo().save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        return super.getLinks(links);
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
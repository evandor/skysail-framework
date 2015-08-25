package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.utils.ResourceUtils;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    protected TodoApplication app;

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (TodoApplication) getApplication();
        getResourceContext().addDisabledAjaxNavigation("Todo-Lists", ListsResource.class);
    }

    @Override
    public Todo createEntityTemplate() {
        return new Todo(getQuery(), getAttribute(TodoApplication.LIST_ID), ResourceUtils.determineLocale(this));
    }

    @Override
    public SkysailResponse<?> addEntity(Todo entity) {
        Locale locale = ResourceUtils.determineLocale(this);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"),locale);
        entity.setCreated(cal.getTime());new Date();
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setStatus(Status.NEW);
        entity.setRank(1);
        entity.setParent(getAttribute(TodoApplication.LIST_ID));
        if (entity.getImportance() == null) {
            entity.setImportance(50);
        }
        entity.setUrgency(Ranker.calcUrgency(entity));
        String id = app.getRepository().add(entity, "parent").toString();
        entity.setId(id);
        return new SkysailResponse<String>();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class);
    }

    @Override
    public String redirectTo() {
        if ("submitAndNew".equals(submitValue)) {
            return super.redirectTo(PostTodoResource.class);
        }
        return super.redirectTo(TodosResource.class);
    }

}
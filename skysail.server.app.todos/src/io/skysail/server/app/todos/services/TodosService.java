package io.skysail.server.app.todos.services;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.repo.*;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ResourceUtils;

import java.util.*;
import java.util.stream.IntStream;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class TodosService {

    private ListsRepository listRepo;
    private TodosRepository todosRepo;

    public TodosService(ListsRepository listRepo, TodosRepository todosRepo) {
        this.listRepo = listRepo;
        this.todosRepo = todosRepo;
    }

    public SkysailResponse<Todo> addTodo(PostTodoResource postTodoResource, Todo entity) {
        TodoApplication app = (TodoApplication) postTodoResource.getApplication();
        TodoList list = app.getListRepo().findOne(postTodoResource.getAttribute("id"));

        Locale locale = ResourceUtils.determineLocale(postTodoResource);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"),locale);
        entity.setCreated(cal.getTime());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setStatus(Status.NEW);
        entity.setRank(1);
        if (entity.getImportance() == null) {
            entity.setImportance(50);
        }
        entity.setUrgency(Ranker.calcUrgency(entity));

        list.getTodos().add(entity);
        listRepo.update(list.getId(), list, "todos").toString();
        return new SkysailResponse<>();
    }

    public Todo getTodo(SkysailServerResource<?> resource, String todoId) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        Todo todo = todosRepo.findOne(todoId);
        todo.setViews(todo.getViews() != null ? 1 + todo.getViews() : 1);
        app.getTodosRepo().update(todo.getId(), todo, "todos");
        return todo;
    }

    public SkysailResponse<Todo> update(PutTodoResource resource, Todo entityFromTheWire) {
        TodoList list = listRepo.findOne(entityFromTheWire.getParent());

        Todo entityToBeUpdated = resource.getEntity(null);
        resource.copyProperties(entityToBeUpdated, entityFromTheWire);
        entityToBeUpdated.setModified(new Date());
        entityToBeUpdated.setUrgency(Ranker.calcUrgency(entityToBeUpdated));
        Integer views = entityToBeUpdated.getViews();
        if (views == null) {
            entityToBeUpdated.setViews(1);
        }

        OptionalInt indexIfExisting = IntStream.range(0, list.getTodos().size())
                .filter(index -> list.getTodos().get(index).getId().equals(entityToBeUpdated.getId())).findFirst();
        if (indexIfExisting.isPresent()) {
            list.getTodos().set(indexIfExisting.getAsInt(), entityToBeUpdated);
        } else {
            list.getTodos().add(entityToBeUpdated);
        }

        listRepo.update(list.getId(), list, "todos");

        return new SkysailResponse<>(entityToBeUpdated);
    }

    public SkysailResponse<String> delete(TodoResource resource, String id) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        app.getTodosRepo().delete(id);
        return new SkysailResponse<String>();

    }
}

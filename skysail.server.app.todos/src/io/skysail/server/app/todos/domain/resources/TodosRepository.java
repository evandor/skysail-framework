package io.skysail.server.app.todos.domain.resources;

import io.skysail.server.app.todos.domain.Todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true, properties = "name=TodosRepository")
public class TodosRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.setupVertices(Todo.class.getSimpleName());
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        TodosRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        TodosRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return dbService.findObjects(cls, "username");
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public void update(Todo entity) {
        dbService.update(entity.getId(), entity);
    }

    public Object add(Todo entity) {
        return dbService.persist(entity);
    }

    // public List<Todo> getTodos(String username) {
    // String sql = "SELECT from Todo WHERE owner= :username";
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("username", username);
    // return null;// dbService.findAll(sql, Todo.class, params);
    // }

    public List<String> getTodosAsJson(String username) {
        String sql = "SELECT from Todo WHERE owner= :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return null;// dbService.findAndReturnJson(sql, Todo.class, params);
    }

    public Todo getById(String id) {
        return dbService.findObjectById(Todo.class, id);
    }

    public String getJsonById(String id) {
        return null;// dbService.findAndReturnJson(id, Todo.class);
    }

    public void delete(String id) {
        dbService.delete(Todo.class, id);
    }

}
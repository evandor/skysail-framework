package io.skysail.server.app.todos.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService2;

@Component
public class TodosRepository {

    private static TodosRepository instance;

    private DbService2 dbService;

    public static TodosRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new TodosRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        TodosRepository.instance = this;
    }

    @Deactivate
    public void deactivate() {
        TodosRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        this.dbService = null;
    }

    public Object add(Todo entity) {
        return dbService.persist(entity);
    }

    public List<Todo> getTodos(String username) {
        String sql = "SELECT from Todo WHERE owner= :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return null;// dbService.findAll(sql, Todo.class, params);
    }

    public List<String> getTodosAsJson(String username) {
        String sql = "SELECT from Todo WHERE owner= :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return null;// dbService.findAndReturnJson(sql, Todo.class, params);
    }

    public Todo getById(String id) {
        return null;// dbService.find(id, Todo.class);
    }

    public String getJsonById(String id) {
        return null;// dbService.findAndReturnJson(id, Todo.class);
    }

    public void update(Todo entity) {
        // dbService.update(entity);
    }

    public void delete(String id) {
        // dbService.delete(id);
    }

}
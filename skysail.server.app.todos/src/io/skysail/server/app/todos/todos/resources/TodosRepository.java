package io.skysail.server.app.todos.todos.resources;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;

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
        dbService.setupVertices(Todo.class.getSimpleName(),TodoList.class.getSimpleName());
        dbService.register(Todo.class,TodoList.class);
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        TodosRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        TodosRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls, String listId, String sorting) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        
        String sql = "SELECT from "+cls.getSimpleName()+" WHERE " + getWhereStatement(listId)+ " " + sorting;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("list", listId);
        return dbService.findObjects(sql, params);
    }
    

    public List<TodoList> findAllLists() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        String sql = "SELECT from "+TodoList.class.getSimpleName()+" WHERE owner= :username ORDER BY name ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return dbService.findObjects(sql, params);
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public void update(String id, Object entity) {
        dbService.update(id, entity);
    }

    public Object add(Todo entity) {
         Object result = dbService.persist(entity);
         increaseOtherTodosRank(entity);
         return result;
    }

    private void increaseOtherTodosRank(Todo entity) {
        String sql = "update " + Todo.class.getSimpleName() + " INCREMENT rank = 1 WHERE owner = :username AND rank >= :referenceRank";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", entity.getOwner());
        params.put("referenceRank", entity.getRank());
        dbService.executeUpdate(sql, params);
    }

    public void delete(Class<?> cls, String id) {
        dbService.delete(cls, id);
    }

    public long getTodosCount(String username,String listId) {
        String sql = "select COUNT(*) as count from " + Todo.class.getSimpleName() + " WHERE " + getWhereStatement(listId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("list", listId);
        return dbService.getCount(sql, params);
    }

    private String getWhereStatement(String listId) {
        if ("null".equals(listId)) {
            return "owner= :username AND list IS NULL";
        }
        return "owner= :username AND list= :list";
    }
  
}
package io.skysail.server.app.todos.repo;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.db.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.repo.DbRepository;

import java.util.*;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=ListsRepository")
public class ListsRepository extends GraphDbRepository<TodoList> implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        dbService.createWithSuperClass("V", TodoList.class.getSimpleName());
        dbService.register(TodoList.class);
        dbService.createUniqueIndex(TodoList.class, "name", "owner");
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    public List<TodoList> findAllLists(Filter filter) {
        return findAllLists(filter, new Pagination());
    }

    public List<TodoList> findAllLists(Filter filter, Pagination pagination) {
        // TODO do this in one statement
        String sql = "SELECT from " + TodoList.class.getSimpleName() + " WHERE "+filter.getPreparedStatement()+" ORDER BY name "
                + limitClause(pagination);
        List<TodoList> lists = dbService.findObjects(sql, filter.getParams());
        for (TodoList list : lists) {
            addCount(filter, list);
        }
        return lists;
    }

    private void addCount(TodoList list) {
        String sql;
        Map<String, Object> params;
        sql = "SELECT in('parent').size() as count from " + TodoList.class.getSimpleName()
                + " WHERE @rid=:rid";
        params = new HashMap<String, Object>();
        params.put("rid", list.getId());
        long cnt = dbService.getCount(sql, params);
        list.setTodosCount(cnt);
    }

    private void addCount(Filter filter, TodoList list) {
        String sql = "SELECT COUNT(*) as count from " + Todo.class.getSimpleName()
                + " WHERE "+list.getId()+" IN out('parent') AND status <> '"+Status.ARCHIVED+"' AND "+filter.getPreparedStatement();
        Map<String, Object> params = filter.getParams();
        params.put("list", list.getId().replace("#",""));
        long cnt = dbService.getCount(sql, params);
        list.setTodosCount(cnt);
    }

    public <T> T getById(Class<?> cls, String id) {
        T list = dbService.findObjectById(cls, id);
        if (list != null && cls.equals(TodoList.class)) {
            addCount((TodoList) list);
        }
        return list;
    }

    public long getTodosCount(String listId, Filter filter) {
        String sql = "select COUNT(*) as count from " + Todo.class.getSimpleName() + " WHERE "
                + filter.getPreparedStatement();
        return dbService.getCount(sql, filter.getParams());
    }

    public long getListsCount(Filter filter) {
        String sql = "select COUNT(*) as count from " + TodoList.class.getSimpleName() + " WHERE " + filter.getPreparedStatement();
        return dbService.getCount(sql, filter.getParams());
    }
}
package io.skysail.server.app.todos.repo;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.db.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.*;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=TodosRepository")
public class TodosRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() { // NO_UCD
        dbService.createWithSuperClass("V", Todo.class.getSimpleName(), TodoList.class.getSimpleName());
        //dbService.createWithSuperClass("E", vertices);
        dbService.register(Todo.class, TodoList.class);
        dbService.createUniqueIndex(TodoList.class, "name", "owner");
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        TodosRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        TodosRepository.dbService = null;
    }

    public List<TodoList> findAllLists(Filter filter, Pagination pagination) {
        // TODO do this in one statement
        String sql = "SELECT from " + TodoList.class.getSimpleName() + " WHERE "+filter.getPreparedStatement()+" ORDER BY name "
                + limitClause(pagination.getLinesPerPage(),pagination.getPage());
        Map<String, Object> params = new HashMap<String, Object>();
        List<TodoList> lists = dbService.findObjects(sql, filter.getParams());
        for (TodoList list : lists) {
            addCount(filter, list);
        }
        return lists;
    }



    public List<Todo> findAllTodos(Filter filter, Pagination pagination) {
        String sql = "SELECT *, SUM(urgency,importance) as rank from " + Todo.class.getSimpleName() + " WHERE "+filter.getPreparedStatement()+" ORDER BY rank DESC "
                + limitClause(pagination.getLinesPerPage(),pagination.getPage());

        //sql = "SELECT *, SUM(urgency,importance) as rank from Todo WHERE NOT (status=:status) AND owner=:owner AND #17:0 IN out('parent') ORDER BY rank DESC SKIP 0 LIMIT 10";
        return dbService.findObjects(sql, filter.getParams());
    }

    private String limitClause(long linesPerPage, int page) {
        if (linesPerPage <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("SKIP " + linesPerPage * (page-1) + " LIMIT " + linesPerPage);
        return sb.toString();
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

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        T list = dbService.findObjectById(cls, id);
        if (list != null && cls.equals(TodoList.class)) {
            addCount((TodoList) list);
        }
        return list;
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
        String sql = "update " + Todo.class.getSimpleName()
                + " INCREMENT rank = 1 WHERE owner = :username AND rank >= :referenceRank";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", entity.getOwner());
        params.put("referenceRank", entity.getRank());
        dbService.executeUpdate(sql, params);
    }

    public void delete(Class<?> cls, String id) {
        dbService.delete(cls, id);
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

    private String getWhereStatement(String listId) {
        if ("null".equals(listId)) {
            return "list IS NULL";
        }
        return "list= :list";
    }

}
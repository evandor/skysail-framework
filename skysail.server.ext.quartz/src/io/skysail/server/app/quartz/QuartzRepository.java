package io.skysail.server.app.quartz;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.quartz.groups.Group;
import io.skysail.server.db.DbService;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.List;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=QuartzRepository")
public class QuartzRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() { // NO_UCD
       dbService.createWithSuperClass("V", Group.class.getSimpleName());
       dbService.register(Group.class);
    // dbService.createUniqueIndex(TodoList.class, "name", "owner");
    }

    @Reference
    public void setDbService(DbService dbService) {
        QuartzRepository.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        QuartzRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<?> cls, Filter filter, Pagination pagination) {
        String sql =
                "SELECT * from " + cls.getSimpleName() +
                where(filter) +
                " ORDER BY rank DESC " + limitClause(pagination);
        return dbService.findObjects(sql, filter.getParams());
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    // public List<Todo> findAllTodos(Filter filter, Pagination pagination) {
    // String sql =
    // "SELECT *, SUM(urgency,importance, views) as rank, out('parent') as parent from "
    // + Todo.class.getSimpleName() +
    // " WHERE "+filter.getPreparedStatement()+
    // " ORDER BY rank DESC "
    // + limitClause(pagination.getLinesPerPage(),pagination.getPage());
    //
    // //sql =
    // "SELECT *, SUM(urgency,importance) as rank from Todo WHERE NOT (status=:status) AND owner=:owner AND #17:0 IN out('parent') ORDER BY rank DESC SKIP 0 LIMIT 10";
    // return dbService.findObjects(sql, filter.getParams());
    // }

    private String limitClause(Pagination pagination) { //long linesPerPage, int page) {
        if (pagination == null) {
            return "";
        }
        long linesPerPage = pagination.getLinesPerPage();
        int page = pagination.getPage();
        if (linesPerPage <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("SKIP " + linesPerPage * (page - 1) + " LIMIT " + linesPerPage);
        return sb.toString();
    }

    // private void addCount(TodoList list) {
    // String sql;
    // Map<String, Object> params;
    // sql = "SELECT in('parent').size() as count from " +
    // TodoList.class.getSimpleName()
    // + " WHERE @rid=:rid";
    // params = new HashMap<String, Object>();
    // params.put("rid", list.getId());
    // long cnt = dbService.getCount(sql, params);
    // list.setTodosCount(cnt);
    // }

    // private void addCount(Filter filter, TodoList list) {
    // String sql = "SELECT COUNT(*) as count from " +
    // Todo.class.getSimpleName()
    // +
    // " WHERE "+list.getId()+" IN out('parent') AND status <> '"+Status.ARCHIVED+"' AND "+filter.getPreparedStatement();
    // Map<String, Object> params = filter.getParams();
    // params.put("list", list.getId().replace("#",""));
    // long cnt = dbService.getCount(sql, params);
    // list.setTodosCount(cnt);
    // }

    public static Object add(Identifiable entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    // public <T> T getById(Class<?> cls, String id) {
    // T list = dbService.findObjectById(cls, id);
    // if (list != null && cls.equals(TodoList.class)) {
    // addCount((TodoList) list);
    // }
    // return list;
    // }

    public Object update(String id, Identifiable entity, String... edges) {
        return dbService.update(id, entity, edges);
    }

    // public Object add(Todo entity) {
    // Object result = dbService.persist(entity);
    // increaseOtherTodosRank(entity);
    // return result;
    // }

    // private void increaseOtherTodosRank(Todo entity) {
    // String sql = "update " + Todo.class.getSimpleName()
    // +
    // " INCREMENT rank = 1 WHERE owner = :username AND rank >= :referenceRank";
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("username", entity.getOwner());
    // params.put("referenceRank", entity.getRank());
    // dbService.executeUpdate(sql, params);
    // }

    public void delete(Class<?> cls, String id) {
        dbService.delete(cls, id);
    }

    // public long getTodosCount(String listId, Filter filter) {
    // String sql = "select COUNT(*) as count from " +
    // Todo.class.getSimpleName() + " WHERE "
    // + filter.getPreparedStatement();
    // return dbService.getCount(sql, filter.getParams());
    // }
    //
    // public long getListsCount(Filter filter) {
    // String sql = "select COUNT(*) as count from " +
    // TodoList.class.getSimpleName() + " WHERE " +
    // filter.getPreparedStatement();
    // return dbService.getCount(sql, filter.getParams());
    // }

    private String getWhereStatement(String listId) {
        if ("null".equals(listId)) {
            return "list IS NULL";
        }
        return "list= :list";
    }

    public long getCount(Class<?> cls, Filter filter) {
        String sql = "select COUNT(*) as count from " + cls.getSimpleName() + where(filter);
        return dbService.getCount(sql, filter.getParams());
    }

    private String where(Filter filter) {
        if (filter.getPreparedStatement() == null) {
            return "";
        }
        return " WHERE " + filter.getPreparedStatement();
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    // public Object getVertexById(Class<TodoList> cls, String id) {
    // return
    // dbService.findGraphs("SELECT FROM "+cls.getSimpleName()+" WHERE @rid="+id);
    // }

}
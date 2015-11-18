package io.skysail.server.app.todos.repo;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.db.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=TodosRepository")
@Slf4j
public class TodosRepository extends GraphDbRepository<Todo>  implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        super.activateWithLongName(Todo.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        log.debug("setting dbService");
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        log.debug("unsetting dbService");
        this.dbService = null;
    }

    public List<Todo> findAllTodos(Filter filter) {
        return findAllTodos(filter, new Pagination());
    }

    public List<Todo> findAllTodos(Filter filter, Pagination pagination) {
        String sql =
                "SELECT *, SUM(urgency,importance, views) as rank from " + Todo.class.getName().replace(".", "_") +
                " WHERE "+filter.getPreparedStatement()+
                " ORDER BY rank DESC "
                + limitClause(pagination)
                //+ " FETCHPLAN *:-1"
                ;
        return dbService.findGraphs(Todo.class, sql, filter.getParams());
    }

    @Override
    public Object save(Todo entity, String... edges) {
        Object result = super.save(entity, edges);
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
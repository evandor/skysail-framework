package io.skysail.server.app.todos.repo;

import java.util.List;

import org.osgi.service.component.annotations.*;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.db.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=ListsRepository")
@Slf4j
public class ListsRepository extends GraphDbRepository<TodoList> implements DbRepository {

    @Activate
    public void activate() { // NO_UCD
        log.debug("activating Repository");
        dbService.createWithSuperClass("V", DbClassName.of(TodoList.class));
        dbService.register(TodoList.class);
        dbService.createUniqueIndex(TodoList.class, "name", "owner");
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

    public List<TodoList> findAllLists(Filter filter) {
        return findAllLists(filter, new Pagination());
    }

    public List<TodoList> findAllLists(Filter filter, Pagination pagination) {
        String sql =
                "SELECT from " + DbClassName.of(TodoList.class) +
                "  WHERE "+filter.getPreparedStatement() +
                "  ORDER BY name "
                +  limitClause(pagination);
        return dbService.<TodoList> findGraphs(TodoList.class, sql, filter.getParams());
    }


    public <T> T getById(Class<?> cls, String id) {
        return dbService.findById2(cls, id);
    }

    public long getTodosCount(String listId, Filter filter) {
        String sql = "select COUNT(*) as count from " + Todo.class.getName().replace(".","_") + " WHERE "
                + filter.getPreparedStatement();
        return dbService.getCount(sql, filter.getParams());
    }

    public long getListsCount(Filter filter) {
        String sql = "select COUNT(*) as count from " + TodoList.class.getName().replace(".","_") + " WHERE " + filter.getPreparedStatement();
        return dbService.getCount(sql, filter.getParams());
    }
}
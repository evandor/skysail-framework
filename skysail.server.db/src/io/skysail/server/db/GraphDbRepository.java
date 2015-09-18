package io.skysail.server.db;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.lang.reflect.*;
import java.util.*;


public class GraphDbRepository<T> implements DbRepository {

    protected DbService dbService;

    private Class<?> entityType;

    public GraphDbRepository() {
        entityType = getParameterizedType();
    }

    public Object save(T entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public Object update(String id, Object entity, String... edges) {
        return dbService.update(id, entity, edges);
    }

    public void delete(String id) {
        dbService.delete(entityType, id);
    }

    public T getById(String id) {
        return dbService.findById(entityType, id);
    }

    public Object getVertexById(String id) {
        return dbService.findGraphs("SELECT FROM "+entityType.getSimpleName()+" WHERE @rid="+id);
    }

    public List<T> find(Filter filter) {
        return find(filter, new Pagination());
    }

    public List<T> find(Filter filter, Pagination pagination) {
        String sql =
                "SELECT * from " + entityType.getSimpleName() +
                " WHERE "+filter.getPreparedStatement()+
                " "
                + limitClause(pagination);
        return dbService.findObjects(sql, filter.getParams());
    }

    protected String limitClause(Pagination pagination) {
        if (pagination == null) {
            return "";
        }
        long linesPerPage = pagination.getLinesPerPage();
        long page = pagination.getPage();
        if (linesPerPage <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("SKIP " + linesPerPage * (page-1) + " LIMIT " + linesPerPage);
        return sb.toString();
    }


    private Class<?> getParameterizedType() {
        ParameterizedType parameterizedType = getParameterizedType(getClass());
        Type firstActualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (firstActualTypeArgument.getTypeName().startsWith("java.util.Map")) {
            return Map.class;
        }
        return (Class<?>) firstActualTypeArgument;
    }

    private ParameterizedType getParameterizedType(Class<?> cls) {
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        }
        return getParameterizedType(cls.getSuperclass());
    }
}

package io.skysail.server.db;

import java.lang.reflect.*;
import java.util.Map;


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
        return dbService.findObjectById(entityType, id);
    }

    public Object getVertexById(String id) {
        return dbService.findGraphs("SELECT FROM "+entityType.getSimpleName()+" WHERE @rid="+id);
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

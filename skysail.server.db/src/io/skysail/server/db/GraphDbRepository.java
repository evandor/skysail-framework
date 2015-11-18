package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.restlet.engine.util.StringUtils;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@Slf4j
public class GraphDbRepository<T extends Identifiable> implements DbRepository {

    protected DbService dbService;

    private Class<?> entityType;

    public GraphDbRepository() {
        entityType = getParameterizedType();
        //entityType = ReflectionUtils.getParameterizedType(getClass());
    }

    @Deprecated // use long name
    public void activate(Class<?>... classes) {
        log.debug("activating repository for class(es) {}", Arrays.stream(classes).map(Class::getName).collect(Collectors.joining(",")));
        Arrays.stream(classes).forEach(cls -> {
            try {
                dbService.createWithSuperClass("V", cls.getSimpleName());
                dbService.register(cls);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void activateWithLongName(Class<?>... classes) {
        log.debug("activating repository for class(es) {}", Arrays.stream(classes).map(Class::getName).collect(Collectors.joining(",")));
        Arrays.stream(classes).forEach(cls -> {
            try {
                dbService.createWithSuperClass("V", cls.getName().replace(".", "_"));
                dbService.register(cls);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }


    @Override
    public Class<Identifiable> getRootEntity() {
        return (Class<Identifiable>)entityType;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    public Object save(T entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    @Override
    public Object save(Identifiable entity) {
        return dbService.persist(entity);
    }

    public Object update(String id, Identifiable entity, String... edges) {
        return dbService.update(id, entity, edges);
    }

    public void delete(String id) {
        dbService.delete(entityType, id);
    }

    public void deleteGraph(String id) {
        dbService.deleteGraph(entityType, id);
    }

    public void deleteVertex(String id) {
        dbService.deleteVertex(id);
    }

    @Override
    public T findOne(String id) {
        return dbService.findById(entityType, id);
    }

    @Deprecated // use findOne
    public T getById(String id) {
        return dbService.findById(entityType, id);
    }

    public Object getVertexById(String id) {
         List<Object> found = dbService.findGraphs(entityType, "SELECT FROM "+entityType.getName().replace(".",  "_") +" WHERE @rid="+id);
         if (found.isEmpty()) {
             return null;
         }
         if (found.size() > 1) {
             throw new IllegalStateException();
         }
         return found.get(0);
    }

    public List<T> find(Filter filter) {
        return find(filter, new Pagination());
    }

    public List<T> find(Filter filter, Pagination pagination) {
        String sql =
                "SELECT * from " + entityType.getSimpleName() +
                (!StringUtils.isNullOrEmpty(filter.getPreparedStatement()) ? " WHERE "+filter.getPreparedStatement() : "") + " " +
                limitClause(pagination);
        return dbService.findObjects(sql, filter.getParams());
    }

    public List<T> findVertex(Filter filter) {
        return findVertex(filter, new Pagination());
    }

    public List<T> findVertex(Filter filter, Pagination pagination) {
        String sql = "SELECT * from " + entityType.getSimpleName() +
                (!StringUtils.isNullOrEmpty(filter.getPreparedStatement()) ? " WHERE "+filter.getPreparedStatement() : "") + " " +
                limitClause(pagination);
        List<Object> entities = dbService.findGraphs(entityType.getClass(), sql, filter.getParams());
        List<T> result = new ArrayList<>();
        entities.stream().map(OrientVertex.class::cast).forEach(vertex -> {
            result.add(((OrientGraphDbService)dbService).vertexToBean(vertex, entityType));
        });
        return result;
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

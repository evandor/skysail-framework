package io.skysail.server.db;

import java.util.*;
import java.util.stream.Collectors;

import org.restlet.engine.util.StringUtils;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GraphDbRepository<T extends Identifiable> implements DbRepository {

    protected DbService dbService;

    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public GraphDbRepository() {
        entityType = (Class<T>) ReflectionUtils.getParameterizedType(getClass());
    }

    public void activate(Class<?>... classes) {
        log.debug("activating repository for class(es) {}", Arrays.stream(classes).map(Class::getName).collect(Collectors.joining(",")));
        Arrays.stream(classes).forEach(cls -> {
            try {
                dbService.createWithSuperClass("V", DbClassName.of(cls));
                dbService.register(cls);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Identifiable> getRootEntity() {
        return (Class<Identifiable>)entityType;
    }

    public OrientVertex save(T entity, String... edges) {
        return (OrientVertex) dbService.persist(entity, edges);
    }

    @Override
    public Object save(Identifiable entity) {
        return dbService.persist(entity);
    }

    @Override
    public Object update(String id, Identifiable entity, String... edges) {
        return dbService.update(id, entity, edges);
    }

    public void delete(String id) {
        dbService.delete2(entityType, id);
    }

    public void deleteVertex(String id) {
        dbService.deleteVertex(id);
    }

    @Override
    public T findOne(String id) {
        return dbService.findById2(entityType, id);
    }

    public Object getVertexById(String id) {
        return dbService.findGraphs(entityType.getClass(), "SELECT FROM "+entityType.getSimpleName()+" WHERE @rid="+id);
    }

    public List<T> find(Filter filter) {
        return find(filter, new Pagination());
    }

    public List<T> find(Filter filter, Pagination pagination) {
        String sql =
                "SELECT * from " + DbClassName.of(entityType) +
                (!StringUtils.isNullOrEmpty(filter.getPreparedStatement()) ? " WHERE "+filter.getPreparedStatement() : "") + " " +
                limitClause(pagination);

        return dbService.findGraphs(entityType, sql, filter.getParams());
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

}

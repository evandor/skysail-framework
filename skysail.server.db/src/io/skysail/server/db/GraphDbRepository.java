package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.utils.SkysailBeanUtils;

import java.lang.reflect.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@Slf4j
public class GraphDbRepository<T extends Identifiable> implements DbRepository {

    protected DbService dbService;

    private Class<?> entityType;

    public GraphDbRepository() {
        entityType = getParameterizedType();
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

    public Object update(String id, Object entity, String... edges) {
        return dbService.update(id, entity, edges);
    }

    public void delete(String id) {
        dbService.delete(entityType, id);
    }

    @Override
    public Identifiable findOne(String id) {
        return dbService.findById(entityType, id);
    }

    @Deprecated // use findOne
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

    public List<T> findVertex(Filter filter) {
        return findVertex(filter, new Pagination());
    }

    public List<T> findVertex(Filter filter, Pagination pagination) {
        String sql = "SELECT * from " + entityType.getSimpleName() + " WHERE " + filter.getPreparedStatement() + " "
                + limitClause(pagination);
        // return dbService.findObjects(sql, filter.getParams());
        List<Object> entities = dbService.findGraphs(sql, filter.getParams());
        List<T> result = new ArrayList<>();
        entities.stream().map(OrientVertex.class::cast).forEach(vertex -> {
            ODocument record = vertex.getRecord();
            System.out.println(record);
            Map<String, Object> map = record.toMap();
            T bean;
            try {
                bean = (T) entityType.newInstance();
                SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(bean, Locale.getDefault());
                beanUtilsBean.populate(bean, map);
                result.add(bean);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
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

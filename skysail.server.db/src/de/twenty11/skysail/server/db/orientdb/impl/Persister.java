package de.twenty11.skysail.server.db.orientdb.impl;

import java.lang.reflect.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;

@Slf4j
public class Persister {

    private OrientGraph db;
    private List<String> edges;

    public Persister(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
    }

    public <T> Object persist(T entity) {
        return runInTransaction(entity);
    }

    private <T> Object execute(Object entity) {
        Vertex vertex = db.addVertex("class:" + entity.getClass().getSimpleName());
        try {
            Map<String, String> properties = BeanUtils.describe(entity);
            properties.keySet().stream().forEach(key -> {
                if ("id".equals(key)) {
                    return;
                }
                if (!edges.contains(key)) {
                    if (properties.get(key) != null && !("class".equals(key))) {
                        setProperty(entity, vertex, properties, key);
                    }
                } else {
                    OrientVertex target = db.getVertex(properties.get(key));
                    if (target != null) {
                        db.addEdge(null, vertex, target, key);
                    }
                }
            });
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Problem when persisting entity",  e);
        }
    }

    private void setProperty(Object entity, Vertex vertex, Map<String, String> properties, String key) {
        try {
            setVertexProperty("get", entity, vertex, key);
        } catch (Exception e) {
            try {
                setVertexProperty("is", entity, vertex, key);
            } catch (Exception e1) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void setVertexProperty(String prefix, Object entity, Vertex vertex, String key) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Method method = entity.getClass().getMethod(getMethodName(prefix, key));
        Object result = method.invoke(entity);
        log.info("setting {}={} [{}]", new Object[] { key, result, result.getClass() });
        vertex.setProperty(key, result);
    }

    private String getMethodName(String prefix, String key) {
        return new StringBuilder(prefix).append(key.substring(0, 1).toUpperCase()).append(key.substring(1)).toString();
    }

    /**
     * Template Method to make sure that the orient db is called correctly.
     *
     * @param db
     * @param entity
     * @return
     */
    private <T> Object runInTransaction(Object entity) {
        try {
            Vertex result = (Vertex)execute(entity);
            db.commit();
            return result.getId();
        } catch (Exception e) {
            db.rollback();
            throw e;
        } finally {
            db.shutdown();
        }
    }

}

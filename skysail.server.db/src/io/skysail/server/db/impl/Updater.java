package io.skysail.server.db.impl;

import io.skysail.api.domain.Identifiable;

import java.lang.reflect.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@Slf4j
public class Updater {

    private OrientGraph db;
    private List<String> edges;
    private EdgeHandler edgeHandler;

    public Updater(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
        edgeHandler = new EdgeHandler(db, edges);
    }

    public <T> Object update(Object entity) {
        return runInTransaction(entity);
    }

    private <T> Object execute(Object entity) {
        String id = ((Identifiable) entity).getId();
        Vertex vertex = db.getVertex(id);
        try {
            Map<String, String> properties = BeanUtils.describe(entity);
            properties.keySet().stream().forEach(key -> {
                if (!edges.contains(key)) {
                    if (properties.get(key) != null && !("class".equals(key))) {
                        setProperty(entity, vertex, properties, key);
                    }
                } else {
                    try {
                        edgeHandler.handleEdges(entity, vertex, properties, key);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Problem when updating entity", e);
        }
    }

    /**
     * Template Method to make sure that the orient db is called correctly.
     */
    private <T> Object runInTransaction(Object entity) {
        try {
            Object result = execute(entity);
            db.commit();
            if (result == null) {
                return null;
            }
            return result; // db.detach(result);
        } catch (Exception e) {
            db.rollback();
            throw new RuntimeException("Database Problem, rolled back transaction", e);
        } finally {
            db.shutdown();
        }
    }

    private void setProperty(Object entity, Vertex vertex, Map<String, String> properties, String key) {
        if (key.equals("id")) {
            return;
        }
        try {
            invokeAndSet(entity, vertex, key, methodWith("get", entity, key));
        } catch (Exception e) {
            try {
                invokeAndSet(entity, vertex, key, methodWith("is", entity, key));
            } catch (Exception inner) {
                log.error(e.getMessage(), inner);
            }
        }
    }

    private Method methodWith(String prefix, Object entity, String key) throws NoSuchMethodException {
        return entity.getClass().getMethod(prefix + key.substring(0, 1).toUpperCase() + key.substring(1));
    }

    private void invokeAndSet(Object entity, Vertex vertex, String key, Method method) throws IllegalAccessException,
            InvocationTargetException {
        Object result = method.invoke(entity);
        log.info("setting {}={} [{}]", new Object[] { key, result, result.getClass() });
        vertex.setProperty(key, result);
    }

}

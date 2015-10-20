package io.skysail.server.db.impl;

import io.skysail.api.domain.Identifiable;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;

@Slf4j
public class Persister {

    private OrientGraph db;
    private List<String> edges;
    private EdgeHandler edgeHandler;
    private ObjectMapper mapper = new ObjectMapper();

    public Persister(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
        edgeHandler = new EdgeHandler((identifiable) -> (OrientVertex) execute(identifiable), db);
    }

    public <T extends Identifiable> Object persist(T entity) {
        return runInTransaction(entity);
    }

    private Object execute(@NonNull Identifiable entity) {
        Vertex vertex = determineVertex(entity);
        try {
            @SuppressWarnings("unchecked")
            Map<String,Object> props = mapper.convertValue(entity, Map.class);
            props.keySet().stream().forEach(setPropertyOrCreateEdge(entity, vertex, props));
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Problem when persisting entity", e);
        }
    }

    private Consumer<? super String> setPropertyOrCreateEdge(Identifiable entity, Vertex vertex,
            Map<String, Object> properties) {
        return key -> {
            if ("id".equals(key)) {
                return;
            }
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
        };
    }

    private Vertex determineVertex(Identifiable entity) {
        Vertex vertex;
        if (entity.getId() != null) {
            vertex = db.getVertex(entity.getId());
        } else {
            vertex = db.addVertex("class:" + entity.getClass().getSimpleName());
        }
        return vertex;
    }

    private void setProperty(Object entity, Vertex vertex, Map<String, Object> properties, String key) {
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

    private void setVertexProperty(String prefix, Object entity, Vertex vertex, String key)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
    private <T> Object runInTransaction(Identifiable entity) {
        try {
            Vertex result = (Vertex) execute(entity);
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

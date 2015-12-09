package io.skysail.server.db.impl;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;

import io.skysail.api.domain.Identifiable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Persister {

    protected OrientGraph db;
    protected List<String> edges;
    protected EdgeHandler edgeHandler;

    private ObjectMapper mapper = new ObjectMapper();

    public Persister(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
        edgeHandler = new EdgeHandler((identifiable) -> (VertexAndEdges) execute(identifiable), db);
    }

    public <T extends Identifiable> VertexAndEdges persist(T entity) {
        return runInTransaction(entity);
    }

    protected VertexAndEdges execute(@NonNull Identifiable entity) {
        OrientVertex vertex = determineVertex(entity);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> props = mapper.convertValue(entity, Map.class);
            props.keySet().stream().forEach(setPropertyOrCreateEdge(entity, vertex, props));
//            List<EdgeManipulation> newEdges = props.keySet().stream().map(key -> {
//                return setPropertyOrCreateEdge2(key, entity, vertex, props);
//            }).flatMap(m -> m.stream()).collect(Collectors.toList());
            return new VertexAndEdges(vertex, new ArrayList<EdgeManipulation>());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Problem when persisting entity", e);
        }
    }

    protected List<EdgeManipulation> setPropertyOrCreateEdge2(String key, Identifiable entity, Vertex vertex, Map<String, Object> properties) {
        if ("id".equals(key)) {
            return new ArrayList<>();
        }
        if (!edges.contains(key)) {
            if (properties.get(key) != null && !("class".equals(key))) {
                setProperty(entity, vertex, key);
            }
        } else {
            try {
                return edgeHandler.handleEdges(entity, vertex, properties, key);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return new ArrayList<>();
    }

    @Deprecated
    protected Consumer<? super String> setPropertyOrCreateEdge(Identifiable entity, Vertex vertex,
            Map<String, Object> properties) {
        return key -> {
            if ("id".equals(key)) {
                return;
            }
            if (!edges.contains(key)) {
                if (properties.get(key) != null && !("class".equals(key))) {
                    setProperty(entity, vertex, key);
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

    protected OrientVertex determineVertex(Identifiable entity) {
        OrientVertex vertex;
        if (entity.getId() != null) {
            vertex = db.getVertex(entity.getId());
        } else {
            vertex = db.addVertex("class:" + entity.getClass().getName().replace(".", "_"));
        }
        return vertex;
    }

    private void setProperty(Object entity, Vertex vertex, String key) {
        try {
            setVertexProperty("get", entity, vertex, key);
        } catch (Exception e) {
            // try "isXXX" instead of "getXXX"
            try {
                setVertexProperty("is", entity, vertex, key);
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
            }
        }
    }

    protected void setVertexProperty(String prefix, Object entity, Vertex vertex, String key)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = entity.getClass().getMethod(getMethodName(prefix, key));
        Object result = method.invoke(entity);
        vertex.setProperty(key, result);
    }

    private static String getMethodName(String prefix, String key) {
        return new StringBuilder(prefix).append(key.substring(0, 1).toUpperCase()).append(key.substring(1)).toString();
    }

    /**
     * Template Method to make sure that the orient db is called correctly.
     *
     * @param db
     * @param entity
     * @return
     */
    private <T> VertexAndEdges runInTransaction(Identifiable entity) {
        try {
            VertexAndEdges result = execute(entity);
            db.commit();
            return result;
        } catch (Exception e) {
            db.rollback();
            throw new RuntimeException("Database Problem, rolled back transaction", e);
        } finally {
            db.shutdown();
        }
    }

}

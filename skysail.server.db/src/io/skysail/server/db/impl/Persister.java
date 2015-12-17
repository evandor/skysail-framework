package io.skysail.server.db.impl;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;

import io.skysail.api.domain.Identifiable;
import io.skysail.domain.core.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Persister {

    protected OrientGraph db;
    protected List<String> edges;
    protected EdgeHandler edgeHandler;

    private ObjectMapper mapper = new ObjectMapper();
    private ApplicationModel applicationModel;

    @Deprecated
    public Persister(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
        edgeHandler = new EdgeHandler((identifiable) -> (OrientVertex) execute(identifiable), db);
    }

    public Persister(OrientGraph db, ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        this.edges = Collections.emptyList();
        this.db = db;
        edgeHandler = new EdgeHandler((identifiable) -> (OrientVertex) execute(identifiable), db);
    }

    public <T extends Identifiable> OrientVertex persist(T entity) {
        return runInTransaction(entity);
    }

    protected OrientVertex execute(@NonNull Identifiable entity) {
        OrientVertex vertex = determineVertex(entity);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> props = mapper.convertValue(entity, Map.class);
            props.keySet().stream().forEach(setPropertyOrCreateEdge(entity, vertex, props));
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Problem when persisting entity", e);
        }
    }


    protected Consumer<? super String> setPropertyOrCreateEdge(Identifiable entity, Vertex vertex,
            Map<String, Object> properties) {
        return key -> {
            if ("id".equals(key)) {
                return;
            }
            if (isProperty(entity, key)) {
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

    private <T> OrientVertex runInTransaction(Identifiable entity) {
        try {
            OrientVertex result = execute(entity);
            db.commit();
            return result;
        } catch (Exception e) {
            db.rollback();
            throw new RuntimeException("Database Problem, rolled back transaction", e);
        } finally {
            db.shutdown();
        }
    }
    
    private boolean isProperty(Identifiable entity, String key) {
        if (applicationModel == null) {
            return !edges.contains(key);            
        }
        EntityModel entityModel = applicationModel.getEntity(entity.getClass().getName());
        if (entityModel == null) {
            return true;
        }
        return !entityModel.getRelations().contains(key);
    }



}

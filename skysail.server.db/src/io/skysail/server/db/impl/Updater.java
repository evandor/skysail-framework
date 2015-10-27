package io.skysail.server.db.impl;

import io.skysail.api.domain.Identifiable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;

@Slf4j
public class Updater extends Persister {

    private ObjectMapper mapper = new ObjectMapper();

    public Updater(OrientGraph db, String[] edges) {
        super(db, edges);
        edgeHandler = new EdgeHandler((identifiable) -> (OrientVertex) execute(identifiable), db);
    }

    protected Consumer<? super String> setPropertyOrCreateEdge(Identifiable entity, Vertex vertex,
            Map<String, Object> properties) {
        return key -> {
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

//    protected Vertex determineVertex(Identifiable entity) {
//        Vertex vertex;
//        if (entity.getId() != null) {
//            vertex = db.getVertex(entity.getId());
//        } else {
//            vertex = db.addVertex("class:" + entity.getClass().getSimpleName());
//        }
//        return vertex;
//    }

    private void setProperty(Object entity, Vertex vertex, Map<String, Object> properties, String key) {
        if (key.equals("id")) {
            return;
        }
        try {
            setVertexProperty("get", entity, vertex, key);
        } catch (Exception e) {
            try {
                setVertexProperty("is", entity, vertex, key);
            } catch (Exception inner) {
                log.error(e.getMessage(), inner);
            }
        }
    }

//    private Method methodWith(String prefix, Object entity, String key) throws NoSuchMethodException {
//        return entity.getClass().getMethod(prefix + key.substring(0, 1).toUpperCase() + key.substring(1));
//    }

//    private void invokeAndSet(Object entity, Vertex vertex, String key, Method method) throws IllegalAccessException,
//            InvocationTargetException {
//        Object result = method.invoke(entity);
//        log.info("setting {}={} [{}]", new Object[] { key, result, result.getClass() });
//        vertex.setProperty(key, result);
//    }

//    /**
//     * Template Method to make sure that the orient db is called correctly.
//     */
//    private <T> Object runInTransaction(Identifiable entity) {
//        try {
//            Object result = execute(entity);
//            db.commit();
//            if (result == null) {
//                return null;
//            }
//            return result; // db.detach(result);
//        } catch (Exception e) {
//            db.rollback();
//            throw new RuntimeException("Database Problem, rolled back transaction", e);
//        } finally {
//            db.shutdown();
//        }
//    }

}

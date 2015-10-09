package io.skysail.server.db.impl;

import io.skysail.api.domain.Identifiable;

import java.lang.reflect.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;

@Slf4j
public class EdgeHandler {

    private OrientGraph db;
    private List<String> edges;

    public EdgeHandler(OrientGraph db, String[] edges) {
        this.db = db;
        this.edges = Arrays.asList(edges);
    }

    public void handleEdges(Object entity, Vertex vertex, Map<String, String> properties, String key)
            throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        
        Field field = entity.getClass().getDeclaredField(key);
        Class<?> type = field.getType();
        Object edges = properties.get(key);
        if (Collection.class.isAssignableFrom(type)) {
            Method method = entity.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
            Collection<Identifiable> references = (Collection<Identifiable>) method.invoke(entity);
            for (Identifiable referencedObject : references) {
                OrientVertex target;
                if (referencedObject.getId() == null) {
                    target = db.addVertex(referencedObject);
                } else {
                    target = db.getVertex(referencedObject.getId());
                }
                db.addEdge(null, vertex, target, key);
            }
        } else if (String.class.isAssignableFrom(type)) { // (edges instanceof
                                                          // String) {
            removeOldReferences(vertex, key);
            addReference(vertex, properties, key, edges);
        }
    }

    private void removeOldReferences(Vertex vertex, String key) {
        Iterable<Edge> edges = vertex.getEdges(Direction.OUT, key);
        if (edges == null) {
            log.info("no edge found for key '{}'", key);
            return;
        }
        for (Edge edge : edges) {
            db.removeEdge(edge);
        }
    }

    private void addReference(Vertex vertex, Map<String, String> properties, String key, Object edge) {
        OrientVertex target = db.getVertex(edge.toString());
        if (target != null) {
            db.addEdge(null, vertex, target, key);
        }
    }

}

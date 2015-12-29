package io.skysail.server.db.impl;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;

import io.skysail.domain.Identifiable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EdgeHandler {

    private OrientGraph db;
    private Function<Identifiable, OrientVertex> fn;

    public EdgeHandler(Function<Identifiable, OrientVertex> fn, OrientGraph db) {
        this.fn = fn;
        this.db = db;
    }

    public void handleEdges(Object entity, Vertex vertex, Map<String, Object> properties, String key)
            throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        
        Field field = entity.getClass().getDeclaredField(key);
        Class<?> type = field.getType();
        Object edges = properties.get(key);
        if (edges == null) {
            return;
        }
        if (Collection.class.isAssignableFrom(type)) {
            Method method = entity.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
            @SuppressWarnings("unchecked")
            Collection<Identifiable> references = (Collection<Identifiable>) method.invoke(entity);
            List<Edge> edgesToDelete = StreamSupport.stream(vertex.getEdges(Direction.OUT, key).spliterator(), false)
                    .collect(Collectors.toList());
            for (Identifiable referencedObject : references) {
                if (referencedObject.getId() != null) {
                    Optional<Edge> edge = match(vertex, db.getVertex(referencedObject.getId()), edgesToDelete, key);
                    if (edge.isPresent()) {
                        edgesToDelete.remove(edge.get());
                    }
                }
            }

            edgesToDelete.stream().forEach(edge -> {
                Vertex targetVertex = edge.getVertex(Direction.IN);
                Iterable<Edge> vertexInEdges = targetVertex.getEdges(Direction.IN);
                Iterator<Edge> iterator = vertexInEdges.iterator();
                iterator.next();
                db.removeEdge(edge);
                if (!iterator.hasNext()) {
                    db.removeVertex(targetVertex);
                }
            });
            
            for (Identifiable referencedObject : references) {
                OrientVertex target = fn.apply(referencedObject);
                Iterable<Edge> existingEdges = vertex.getEdges(Direction.OUT, key);
                if (edgeDoesNotExistYet(existingEdges, vertex, target, key)) {
                    db.addEdge(null, vertex, target, key);
                }
            }
        } else if (String.class.isAssignableFrom(type)) {
            removeOldReferences(vertex, key);
            addReference(vertex, properties, key, edges);
        }
    }

    private Optional<Edge> match(Vertex from, Vertex to, List<Edge> edgesToDelete, String key) {
        return edgesToDelete.stream().filter(edge -> {
            return edgeExists(edge, from, to, key);
        }).findFirst();
    }

    private boolean edgeDoesNotExistYet(Iterable<Edge> existingEdges, Vertex from, Vertex to, String key) {
        if (existingEdges == null) {
            return true;
        }
        return !StreamSupport.stream(existingEdges.spliterator(), false)
                .filter(edge -> edgeExists(edge, from, to, key)).findFirst().isPresent();
    }

    private boolean edgeExists(Edge edge, Vertex from, Vertex to, String key) {
        return key.equals(edge.getLabel()) && edge.getVertex(Direction.IN).equals(to)
                && edge.getVertex(Direction.OUT).equals(from);
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

    private void addReference(Vertex vertex, Map<String, Object> properties, String key, Object edge) {
        OrientVertex target = db.getVertex(edge.toString());
        if (target != null) {
            db.addEdge(null, vertex, target, key);
        }
    }

}

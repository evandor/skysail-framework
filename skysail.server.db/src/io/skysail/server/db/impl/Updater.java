package io.skysail.server.db.impl;

import java.util.*;
import java.util.function.Consumer;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import io.skysail.api.domain.Identifiable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Updater extends Persister {

    public Updater(OrientGraph db, String[] edges) {
        super(db, edges);
        edgeHandler = new EdgeHandler((identifiable) -> (VertexAndEdges) execute(identifiable), db);
    }
    
    protected List<EdgeManipulation> setPropertyOrCreateEdge2(String key, Identifiable entity, Vertex vertex, Map<String, Object> properties) {
        if (!edges.contains(key)) {
            if (properties.get(key) != null && !("class".equals(key))) {
                setProperty(entity, vertex, properties, key);
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

}

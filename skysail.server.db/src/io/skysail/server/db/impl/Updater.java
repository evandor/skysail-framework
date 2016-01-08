package io.skysail.server.db.impl;

import java.util.Map;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.core.ApplicationModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Updater extends Persister {

    @Deprecated
    public Updater(OrientGraph db, String[] edges) {
        super(db, edges);
        edgeHandler = new EdgeHandler((identifiable) -> (OrientVertex) execute(identifiable), db);
    }
    
    public Updater(OrientGraph db, ApplicationModel applicationModel) {
        super(db, applicationModel);
        edgeHandler = new EdgeHandler((identifiable) -> (OrientVertex) execute(identifiable), db);
    }
    
//    protected Consumer<? super String> setPropertyOrCreateEdge(Identifiable entity, Vertex vertex,
//            Map<String, Object> properties) {
//        return key -> {
//            if (!edges.contains(key)) {
//                if (properties.get(key) != null && !("class".equals(key))) {
//                    setProperty(entity, vertex, properties, key);
//                }
//            } else {
//                try {
//                    edgeHandler.handleEdges(entity, vertex, properties, key);
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        };
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

}

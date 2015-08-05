package de.twenty11.skysail.server.db.orientdb.impl;

import io.skysail.api.domain.Identifiable;

import java.lang.reflect.Method;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@Slf4j
public class Updater {

    private OrientGraph db;
    private List<String> edges;

    public Updater(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
    }

    public <T> Object update(Object entity) {
        return runInTransaction(entity);
    }

    private <T> Object execute(Object entity) {
        //return db.save(entity);
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
//                    OrientVertex target = db.getVertex(properties.get("#" + key));
//                    if (target != null) {
//                        db.addEdge(null, vertex, target, key);
//                    }
                }
            });
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }


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
            Object result = execute(entity);
            db.commit();
            if (result == null) {
                return null;
            }
            return result; //db.detach(result);
        } catch (Exception e) {
            db.rollback();
            log.error("Exception in Database, rolled back transaction", e);
            return null;
        } finally {
            db.shutdown();
        }
    }

    private void setProperty(Object entity, Vertex vertex, Map<String, String> properties, String key) {
        if (key.equals("id")) {
            return;
        }
        try {
            Method method = entity.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
            Object result = method.invoke(entity);
            log.info("setting {}={} [{}]", new Object[] { key, result, result.getClass() });
            vertex.setProperty(key, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}


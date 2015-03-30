package de.twenty11.skysail.server.db.orientdb.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@Slf4j
public class Persister {

    private OrientGraph db;
    private List<String> edges;

    public Persister(OrientGraph db, String[] edges) {
        this.edges = Arrays.asList(edges);
        this.db = db;
    }

    public <T> Object persist(T entity) {
        return runInTransaction(entity);
    }

    protected <T> Object execute(Object entity) {
        Vertex vertex = db.addVertex("class:" + entity.getClass().getSimpleName());
        try {
            Map<String, String> properties = BeanUtils.describe(entity);
            properties.keySet().stream().forEach(key -> {
                if (!edges.contains(key)) {
                    if (properties.get(key) != null && !("class".equals(key))) {
                        setProperty(entity, vertex, properties, key);
                    }
                } else {
                    OrientVertex target = db.getVertex(properties.get(key));
                    if (target != null) {
                        db.addEdge(null, vertex, target, key);
                    }
                }
            });
            return vertex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private void setProperty(Object entity, Vertex vertex, Map<String, String> properties, String key) {
        try {
            Method method = entity.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
            Object result = method.invoke(entity);
            log.info("setting {}={} [{}]", new Object[] { key, result, result.getClass() });
            vertex.setProperty(key, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Template Method to make sure that the orient db is called correctly.
     * 
     * @param db
     * @param entity
     * @return
     */
    protected <T> Object runInTransaction(Object entity) {
        try {
            Vertex result = (Vertex)execute(entity);
            db.commit();
            return result.getId();
        } catch (Exception e) {
            db.rollback();
            log.error("Exception in Database, rolled back transaction", e);
            throw e;
        } finally {
            db.shutdown();
        }
    }

}

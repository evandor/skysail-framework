package de.twenty11.skysail.server.db.orientdb.impl;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@Slf4j
public class Persister extends AbstractDbAPI {

    private OrientGraph db;

    public Persister(OrientGraph db) {
        this.db = db;
    }

    public <T> Object persist(T entity) {
        return runInTransaction(db, entity);
    }

    @Override
    protected <T> Object execute(OrientGraph db, Object entity) {
        Vertex vertex = db.addVertex("class:" + entity.getClass().getSimpleName());
        try {
            Map<String, String> properties = BeanUtils.describe(entity);
            properties.keySet().stream().forEach(key -> {
                vertex.setProperty(key, properties.get(key));
            });
            return vertex.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}

package de.twenty11.skysail.server.db.orientdb.impl;

import lombok.extern.slf4j.Slf4j;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@Slf4j
public abstract class AbstractDbAPI {

    protected abstract <T> Object execute(OrientGraph db, Object entity);

    /**
     * Template Method to make sure that the orient db is called correctly.
     * 
     * @param db
     * @param entity
     * @return
     */
    protected <T> Object runInTransaction(OrientGraph db, Object entity) {
        try {
            Object result = execute(db, entity);
            db.commit();
            return result;
        } catch (Exception e) {
            db.rollback();
            log.error("Exception in Database, rolled back transaction", e);
            return null;
        } finally {
            db.shutdown();
        }
    }

}

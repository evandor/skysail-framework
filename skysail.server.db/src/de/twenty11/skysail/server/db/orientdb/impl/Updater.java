package de.twenty11.skysail.server.db.orientdb.impl;

import lombok.extern.slf4j.Slf4j;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Slf4j
public class Updater {

    private OObjectDatabaseTx db;

    public Updater(OObjectDatabaseTx oObjectDatabaseTx) {
        this.db = oObjectDatabaseTx;
    }

    public <T> Object update(Object entity) {
        return runInTransaction(entity);
    }

    protected <T> Object execute(Object entity) {
        // ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        db.save(entity);
        return null;
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
            Object result = execute(entity);
            db.commit();
            return result;
        } catch (Exception e) {
            db.rollback();
            log.error("Exception in Database, rolled back transaction", e);
            return null;
        } finally {
            db.close();
        }
    }

}

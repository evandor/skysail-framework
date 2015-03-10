package de.twenty11.skysail.server.db.orientdb;

import io.skysail.server.db.DbConfigurationProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.db.DbService2;
import de.twenty11.skysail.server.db.orientdb.impl.Persister;
import de.twenty11.skysail.server.db.orientdb.impl.Updater;
import de.twenty11.skysail.server.events.EventHandler;
import de.twenty11.skysail.server.events.SkysailEvents;

@Component(immediate = true)
@Slf4j
public class OrientGraphDbService extends AbstractOrientDbService implements DbService2 {

    private OrientGraphFactory graphDbFactory;

    @Activate
    public void activate() {
        log.debug("activating {}", this.getClass().getName());
        graphDbFactory = new OrientGraphFactory(getDbUrl()).setupPool(1, 10);
        // make sure to create a graphDb object first
        // http://comments.gmane.org/gmane.comp.db.orientdb.user/8588
        // OrientGraph tx = graphDbFactory.getTx();
        // System.out.println(tx);
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        log.debug("activating {}", this.getClass().getName());
        stopDb();
        graphDbFactory = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=skysailgraph)")
    public void setDbConfigurationProvider(DbConfigurationProvider provider) {
        updated(provider);
    }

    public void unsetDbConfigurationProvider(DbConfigurationProvider provider) {
    }

    @Override
    public <T> Object persist(T entity, String... edges) {
        if (entity instanceof DynamicEntity) {
            DynamicEntity dynamicEntity = (DynamicEntity) entity;
            ODatabaseDocumentTx documentDb = getDocumentDb();
            ODocument doc = new ODocument(dynamicEntity.getInstance().getDynaClass().getName());
            dynamicEntity.getProperties().stream().forEach(p -> {
                doc.field(p.getName(), dynamicEntity.getString(p.getName()));
            });
            documentDb.save(doc);
            documentDb.close();
            return doc;
        }
        return new Persister(getDb(), edges).persist(entity);
    }

    // @Override
    // public void persistAsDocument(String doc1) {
    // ODatabaseDocumentTx documentDb = getDocumentDb();
    //
    // ODocument doc = new ODocument("Person");
    // doc.field("name", "Luke");
    // doc.field("surname", "Skywalker");
    // doc.field("city", new ODocument("City").field("name",
    // "Rome").field("country", "Italy"));
    //
    // documentDb.save(doc);
    // documentDb.close();
    // }

    @Override
    public <T> void update(Object id, T entity) {
        new Updater(getObjectDb()).update(entity);
    }

    @Override
    public List<String> getAll(Class<?> cls, String username) {
        OrientGraph db = getDb();
        try {
            List<ODocument> result = db.getRawGraph().query(
                    new OSQLSynchQuery<ODocument>("select from " + cls.getSimpleName()));
            return result.stream().map(doc -> {
                return doc.toJSON();
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            db.shutdown();
        }
    }

    @Override
    public <T> T findObjectById(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        return objectDb.load(new ORecordId(id));
    }

    @Override
    public <T> List<T> findObjects(Class<?> cls, String username) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        return objectDb.query(new OSQLSynchQuery<ODocument>("select from " + cls.getSimpleName()));
    }

    // @Override
    public <T> List<T> findAll(Class<T> entityClass, Class<?>... linkedClasses) {
        return null;
    }

    @Override
    public void delete(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        ORecordId recordId = new ORecordId(id);
        Object loaded = objectDb.load(recordId);
        Object cast = cls.cast(loaded);
        objectDb.delete(recordId);
    }

    protected synchronized void startDb() {
        if (started) {
            return;
        }
        try {
            createDbIfNeeded();
            OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
            db.setLazyLoading(false);
            // registerDefaultClasses();
            initDbIfNeeded();
            started = true;
            if (getDbUrl().startsWith("memory:")) {
                // remark: this might be called without an eventhandler already
                // listening and so the event might be lost
                EventHandler.sendEvent(SkysailEvents.GUI_ALERT_WARNING,
                        "In-Memory database is being used, all data will be lost when application is shut down",
                        "warning");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void stopDb() {
        OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
        if (db != null) {
            db.close();
        }
        started = false;
    }

    private void createDbIfNeeded() {
        if (getDbUrl().startsWith("remote")) {
            Orient.instance().registerEngine(new OEngineRemote());
        }
        OObjectDatabaseTx db = new OObjectDatabaseTx(getDbUrl());
        if (getDbUrl().startsWith("memory:") || getDbUrl().startsWith("plocal")) {
            if (!db.exists()) {
                db.create();
                // importInitialData();
            }
        }
    }

    private void initDbIfNeeded() {
    }

    protected void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopDb();
            }
        });
    }

    @Override
    public void setupVertices(String... vertices) {
        OObjectDatabaseTx objectDb = getObjectDb();
        Arrays.stream(vertices).forEach(v -> {
            if (objectDb.getMetadata().getSchema().getClass(v) == null) {
                OClass vertexClass = objectDb.getMetadata().getSchema().getClass("V");
                objectDb.getMetadata().getSchema().createClass(v).setSuperClass(vertexClass);
            }

        });
    }

    @Override
    public void register(Class<?>... entities) {
        OObjectDatabaseTx db = getObjectDb();
        try {
            Arrays.stream(entities).forEach(entity -> {
                db.getEntityManager().registerEntityClass(entity);
            });
        } finally {
            db.close();
        }
    }

    private OrientGraph getDb() {
        return graphDbFactory.getTx();
    }

    private ODatabaseDocumentTx getDocumentDb() {
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(getDbUrl()).open("admin", "admin");
        ODatabaseRecordThreadLocal.INSTANCE.set(db);
        return db;
    }

    private OObjectDatabaseTx getObjectDb() {
        OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
        return db;
    }

}

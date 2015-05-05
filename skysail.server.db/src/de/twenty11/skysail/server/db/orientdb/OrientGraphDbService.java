package de.twenty11.skysail.server.db.orientdb;

import io.skysail.server.db.DbConfigurationProvider;
import io.skysail.server.db.DbService2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.enhancement.OObjectProxyMethodHandler;
import com.orientechnologies.orient.object.metadata.schema.OSchemaProxyObject;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.db.orientdb.impl.Persister;
import de.twenty11.skysail.server.db.orientdb.impl.Updater;
import de.twenty11.skysail.server.events.EventHandler;

@Component(immediate = true)
@Slf4j
public class OrientGraphDbService extends AbstractOrientDbService implements DbService2 {

    private OrientGraphFactory graphDbFactory;
    private ObjectMapper mapper = new ObjectMapper();

    @Activate
    public void activate() {
        log.debug("activating {}", this.getClass().getName());
        graphDbFactory = new OrientGraphFactory(getDbUrl(), getDbUsername(), getDbPassword()).setupPool(1, 10);
        // make sure to create a graphDb object first
        // http://comments.gmane.org/gmane.comp.db.orientdb.user/8588
        // OrientGraph tx = graphDbFactory.getTx();
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

    @Override
    public <T> void update(Object id, T entity) {
        new Updater(getObjectDb()).update(entity);
    }

    @Override
    public <T> List<T> findObjects(String sql) {
        return findObjects(sql, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> findDocuments(String sql) {
        return findDocuments(sql, new HashMap<>());
    }

    @Override
    public <T> List<T> findObjects(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        List<T> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);

        List<T> detachedEntities = new ArrayList<>();
        for (T t : query) {
            T newOne;
            if (t instanceof DynamicEntity) {

                Method[] methods = t.getClass().getMethods();
                Optional<Method> getHandlerMethod = Arrays.stream(methods)
                        .filter(m -> m.getName().contains("getHandler")).findFirst();
                DynamicEntity de = (DynamicEntity) t;
                if (getHandlerMethod.isPresent()) {
                    DynaBean instance = de.getInstance();
                    Set<EntityDynaProperty> properties = de.getProperties();
                    try {
                        HashMap<String, Object> objectMap = new HashMap<String, Object>();
                        OObjectProxyMethodHandler handler = (OObjectProxyMethodHandler) getHandlerMethod.get()
                                .invoke(t);
                        ODocument doc = handler.getDoc();
                        objectMap = mapper.readValue(doc.toJSON(), new TypeReference<Map<String, Object>>() {
                        });
                        System.out.println(objectMap);
                        for (EntityDynaProperty entityDynaProperty : properties) {
                            Object val = objectMap.get(entityDynaProperty.getName());
                            instance.set(entityDynaProperty.getName(), val != null ? val.toString() : "");
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }
                newOne = (T) de;

            } else {
                newOne = objectDb.detachAll(t, true);
            }
            detachedEntities.add(newOne);
        }
        return detachedEntities;
    }

    @Override
    public List<Map<String, Object>> findDocuments(String sql, Map<String, Object> params) {
        ODatabaseDocumentTx objectDb = getDocumentDb();
        List<ODocument> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);

        List<Map<String, Object>> result = new ArrayList<>();
        for (ODocument t : query) {
            result.add(t.toMap());
        }
        return result;
    }

    @Override
    public <T> T findObjectById(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        return objectDb.load(new ORecordId(id));
    }
    
    @Override
    public ODocument findDocumentById(Class<?> cls, String id) {
        ODatabaseDocumentTx db = getDocumentDb();
        Map<String, Object> params = new HashMap<>();
        params.put("rid", id);
        List<ODocument> query = db.query(new OSQLSynchQuery<ODocument>("SELECT * FROM " + cls.getSimpleName()
                + " WHERE @rid= :rid"), params);
        if (query != null) {
            return query.get(0);
        }
        return null;//new HashMap<String, Object>();
    }

    @Override
    public long getCount(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        List<ODocument> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);
        return query.get(0).field("count");
    }

    @Override
    public void executeUpdate(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.command(new OCommandSQL(sql)).execute(params);
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
            OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), getDbUsername(), getDbPassword());
            db.setLazyLoading(false);
            started = true;
            if (getDbUrl().startsWith("memory:")) {
                // remark: this might be called without an eventhandler already
                // listening and so the event might be lost
                EventHandler.sendEvent(EventHelper.GUI_MSG,
                        "In-Memory database is being used, all data will be lost when application is shut down",
                        "warning");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void stopDb() {
        // OObjectDatabaseTx db =
        // OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
        // if (db != null) {
        // db.close();
        // }
        started = false;
    }

    private void createDbIfNeeded() {
        String dbUrl = getDbUrl();
        if (dbUrl.startsWith("remote")) {
            Orient.instance().registerEngine(new OEngineRemote());
        }
        if (dbUrl.startsWith("memory:") || dbUrl.startsWith("plocal")) {
            final OrientGraphFactory factory = new OrientGraphFactory(dbUrl, getDbUsername(), getDbPassword());
            try {
                OrientGraphNoTx g = factory.getNoTx();
            } finally {
                // this also closes the OrientGraph instances created by the
                // factory
                // Note that OrientGraphFactory does not implement Closeable
                factory.close();
            }
            // OObjectDatabaseTx db = new OObjectDatabaseTx(dbUrl);
            // // OrientGraph db = new OrientGraph(dbUrl);
            // if (!db.exists()) {
            // log.info("creating new database with dbUrl '{}'", dbUrl);
            // db.create();
            //
            // // graphDb.create();
            // // OrientGraph db = new OrientGraph(graphDb);
            //
            // }
        }
    }

    protected void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopDb();
            }
        });
    }

    @Override
    public void createWithSuperClass(String superClass, String... vertices) {
        OObjectDatabaseTx objectDb = getObjectDb();
        try {
            Arrays.stream(vertices).forEach(v -> {
                if (objectDb.getMetadata().getSchema().getClass(v) == null) {
                    OClass vertexClass = objectDb.getMetadata().getSchema().getClass(superClass);
                    objectDb.getMetadata().getSchema().createClass(v).setSuperClass(vertexClass);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void register(Class<?>... entities) {
        OObjectDatabaseTx db = getObjectDb();
        try {
            Arrays.stream(entities).forEach(entity -> {
                log.debug("registering class '{}' @ orientDB", entity);
                db.getEntityManager().registerEntityClass(entity);
            });
        } finally {
            db.close();
        }
    }

    @Override
    public void createUniqueIndex(Class<?> cls, String... fieldnames) {
        OObjectDatabaseTx db = getObjectDb();
        OClass oClass = db.getMetadata().getSchema().getClass(cls);
        Set<String> properties = oClass.propertiesMap().keySet();
        // boolean propertyMissing =
        // Arrays.asList(fieldnames).stream().filter(field -> {
        // if (!(properties.contains(field))) {
        // log.error("cannot create index on non-existing property '" + field
        // +"'");
        // return true;
        // }
        // return false;
        // }).findFirst().isPresent();
        //
        // if (!propertyMissing) {
        // oClass.createIndex("compositeUniqueIndex", INDEX_TYPE.UNIQUE,
        // fieldnames);
        // }

        String indexName = "compositeUniqueIndexNameAndOwner";
        boolean indexExists = oClass.getIndexes().stream().filter(i -> {
            return i.getName().equals(indexName);
        }).findFirst().isPresent();
        if (indexExists) {
            return;
        }
        Arrays.stream(fieldnames).forEach(field -> {
            // TODO need to get types reflectively
                createProperty(cls.getSimpleName(), field, OType.STRING);
            });
        oClass.createIndex(indexName, INDEX_TYPE.UNIQUE, fieldnames);

    }

    private OrientGraph getDb() {
        return graphDbFactory.getTx();
    }

    private ODatabaseDocumentTx getDocumentDb() {
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(getDbUrl()).open(getDbUsername(), getDbPassword());
        ODatabaseRecordThreadLocal.INSTANCE.set(db);
        return db;
    }

    private OObjectDatabaseTx getObjectDb() {
        OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), getDbUsername(), getDbPassword());
        return db;
    }

    @Override
    public void createProperty(String iClassName, String iPropertyName, OType type) {
        OSchemaProxyObject schema = getObjectDb().getMetadata().getSchema();
        OClass cls = schema.getClass(iClassName);
        if (cls == null) {
            cls = schema.createClass(iClassName);
        }
        if (cls.getProperty(iPropertyName) == null) {
            cls.createProperty(iPropertyName, type);
        }
    }

    @Override
    public void update(Map<String, Object> map) {
        ODatabaseDocumentTx documentDb = getDocumentDb();
        ODocument doc = new ODocument().fromMap(map);
        documentDb.save(doc);
        documentDb.commit();
    }

    @Override
    public void update(ODocument doc) {
        ODatabaseDocumentTx documentDb = getDocumentDb();
        //ODocument doc = new ODocument().fromMap(map);
        documentDb.save(doc);
        documentDb.commit();
        
    }

}

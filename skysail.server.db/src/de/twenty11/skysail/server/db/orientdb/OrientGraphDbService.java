package de.twenty11.skysail.server.db.orientdb;

import io.skysail.server.db.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.*;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.*;
import com.orientechnologies.orient.core.sql.functions.OSQLFunction;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.graph.sql.functions.OGraphFunctionFactory;
import com.orientechnologies.orient.object.db.*;
import com.orientechnologies.orient.object.metadata.schema.OSchemaProxyObject;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;

import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.db.orientdb.impl.*;
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

        OGraphFunctionFactory graphFunctions = new OGraphFunctionFactory();
        Set<String> names = graphFunctions.getFunctionNames();

        for (String name : names) {
            OSQLEngine.getInstance().registerFunction(name, graphFunctions.createFunction(name));
            OSQLFunction function = OSQLEngine.getInstance().getFunction(name);
            if (function != null) {
                log.debug("ODB graph function [{}] is registered: [{}]", name, function.getSyntax());
            }
            else {
                log.warn("ODB graph function [{}] NOT registered!!!", name);
            }
        }

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
        return new Persister(getDb(), edges).persist(entity);
    }

    @Override
    public <T> Object update(Object id, T entity, String... edges) {
        return new Updater(getDb(), edges).update(entity);
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
    public <T> List<T> findGraphs(String sql) {
        return findGraphs(sql, new HashMap<>());
    }

    @Override
    public <T> List<T> findObjects(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        List<T> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);

        List<T> detachedEntities = new ArrayList<>();
        for (T t : query) {
            T newOne = objectDb.detachAll(t, true);
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
    public <T> List<T> findGraphs(String sql, Map<String, Object> params) {
        OrientGraph graph = getDb();
        OCommandRequest oCommand = new OCommandSQL(sql);
        Iterable<OrientVertex> execute = graph.command(oCommand).execute(params);

        List<T> detachedEntities = new ArrayList<>();
        Iterator<OrientVertex> iterator = execute.iterator();
        while (iterator.hasNext()) {
            OrientVertex next = iterator.next();
            //T newOne = objectDb.detachAll(t, true);
            OrientElement detached = next.detach();
           // detachedEntities.add((T)detached);
            //System.out.println(next);
        }
        return detachedEntities;
    }

    @Override
    public <T> T findObjectById(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
         T load = objectDb.load(new ORecordId(id));
         return objectDb.detachAll(load, true);
         //return load;
    }

    @Override
    public <T> List<T> findWithGraph(String sql, Class<?> cls, Map<String, Object> params) {
        OrientGraph db = getDb();
        List<T> result = new ArrayList<>();
        Iterable<Vertex> query = (Iterable<Vertex>) db.getVerticesOfClass("Page");
        for (Vertex vertex : query) {
            OrientVertex ov = (OrientVertex)vertex;
            Map<String, Object> record = ov.getRecord().toMap();
            ORecordId id = (ORecordId) record.get("@rid");
            record.put("id", id.toString());
            record.remove("@rid");
            record.remove("@class");
            record.remove("versions");
            @SuppressWarnings("unchecked")
            T convertedValue = (T) mapper.convertValue(record, cls);
            result.add(convertedValue);
        }

        return result;
    }

    @Override
    public long getCount(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        List<ODocument> query = objectDb.query(new OSQLSynchQuery<ODocument>(sql), params);
        Object field = query.get(0).field("count");
        if (field instanceof Long) {
            return (Long)field;
        }
        if (field instanceof Integer) {
            return new Long((Integer)field);
        }
        return 0;
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

    public synchronized void startDb() {
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

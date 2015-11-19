package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.db.impl.*;
import io.skysail.server.utils.SkysailBeanUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.command.traverse.OTraverse;
import com.orientechnologies.orient.core.db.*;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.db.record.ridbag.ORidBag;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.*;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.*;
import com.orientechnologies.orient.core.sql.filter.OSQLPredicate;
import com.orientechnologies.orient.core.sql.functions.OSQLFunction;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.graph.sql.functions.OGraphFunctionFactory;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.metadata.schema.OSchemaProxyObject;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;

import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.events.EventHandler;

@Component(immediate = true)
@Slf4j
public class OrientGraphDbService extends AbstractOrientDbService implements DbService {

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
            } else {
                log.warn("ODB graph function [{}] NOT registered!!!", name);
            }
        }
    }

    @Deactivate
    public void deactivate(ComponentContext context) { // NO_UCD
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
    public Object persist(Identifiable entity, String... edges) {
        return new Persister(getGraphDb(), edges).persist(entity);
    }

    @Override
    public Object update(Object id, Identifiable entity, String... edges) {
        return new Updater(getGraphDb(), edges).persist(entity);
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
        OrientGraph graph = getGraphDb();
        OCommandRequest oCommand = new OCommandSQL(sql);
        Iterable<OrientVertex> execute = graph.command(oCommand).execute(params);

        List<T> detachedEntities = new ArrayList<>();
        Iterator<OrientVertex> iterator = execute.iterator();
        while (iterator.hasNext()) {
            OrientVertex next = iterator.next();
            OrientElement detached = next.detach();
            detachedEntities.add((T) detached);
        }
        return detachedEntities;
    }

    @Override
    public <T> T findObjectById(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        T load = objectDb.load(new ORecordId(id), "*:-1");
        return objectDb.detachAll(load, true);
    }

    @Override
    public <T> T findById(Class<?> cls, String id) {
        OrientGraph graphDb = getGraphDb();
        OrientVertex vertex = graphDb.getVertex(new ORecordId(id));
        return vertexToBean(vertex, cls);
    }

    @Override
    public <T> T findById2(Class<?> cls, String id) {
        ODatabaseDocumentInternal db = getObjectDb().getUnderlying();
        ODatabaseRecordThreadLocal.INSTANCE.set(db);

        // String sql = "SELECT FROM (TRAVERSE OUT() FROM " + id + ")";
        // Map<String, String> params = new HashMap<>();
        OTraverse predicate = new OTraverse().target(new ORecordId(id)).fields("out", "int").limit(1)
                .predicate(new OSQLPredicate("$depth <= 3"));
        OIdentifiable next = predicate.iterator().next();
        ODocument document = (ODocument) next;
        return documentToBean(document, cls);

        // Iterable executed = getGraphDb().command(new
        // OCommandSQL(sql)).execute(params );
        // Object next = executed.iterator().next();
        // System.out.println(next);

        // return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Identifiable> T vertexToBean(OrientVertex vertex, Class<?> beanType) {
        if (vertex == null) {
            return null;
        }
        ODocument record = vertex.getRecord();
        Map<String, Object> entityMap = record.toMap();
        T bean;
        try {
            bean = (T) beanType.newInstance();
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(bean, Locale.getDefault());
            beanUtilsBean.populate(bean, entityMap);
            if (entityMap.get("@rid") != null && bean.getId() == null) {
                bean.setId(entityMap.get("@rid").toString());
            }

            Iterable<Edge> edges = vertex.getEdges(Direction.OUT);
            edges.spliterator().forEachRemaining(
                    e -> {
                        String edgeName = e.getLabel();
                        OrientVertex vertexFromEdge = (OrientVertex) e.getVertex(Direction.IN);
                        try {
                            Class<?> vertexClass = getRegisteredClass(vertexFromEdge.getRecord().getClassName());
                            Identifiable beanFromVertex = vertexToBean(vertexFromEdge, vertexClass);
                            Class<?> fieldType = bean.getClass().getDeclaredField(edgeName).getType();
                            if (Collection.class.isAssignableFrom(fieldType)) {
                                Method collectionGetter = bean.getClass().getMethod(
                                        "get" + edgeName.substring(0, 1).toUpperCase() + edgeName.substring(1));
                                Collection<Identifiable> collection = (Collection<Identifiable>) collectionGetter
                                        .invoke(bean);
                                if (collection == null) {
                                    throw new IllegalStateException(
                                            "could not add to collection object; please make sure your beans field '"
                                                    + edgeName + "' is initialized");
                                }
                                collection.add(beanFromVertex);
                            }
                        } catch (Exception e1) {
                            log.error(e1.getMessage(), e1);
                        }

                    });
            return bean;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private <T extends Identifiable> T documentToBean(ODocument document, Class<?> beanType) {
        Map<String, Object> entityMap = document.toMap();
        T bean;
        try {
            bean = (T) beanType.newInstance();
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(bean, Locale.getDefault());
            beanUtilsBean.populate(bean, entityMap);
            if (entityMap.get("@rid") != null && bean.getId() == null) {
                bean.setId(entityMap.get("@rid").toString());
            }

            List<String> outFields = Arrays.stream(document.fieldNames())
                    .filter(fieldname -> fieldname.startsWith("out_")).collect(Collectors.toList());
            System.out.println(document);
            outFields.forEach(edgeName -> {
                System.out.println(edgeName);
                ORidBag field = document.field(edgeName);
                field.setAutoConvertToRecord(true);
                field.convertLinks2Records();

                ORidBag field2 = document.field(edgeName);
                Iterator<OIdentifiable> iterator = field2.iterator();
                while (iterator.hasNext()) {
                    ODocument edge = (ODocument) iterator.next();
                    System.out.println(edge);
                    ODocument field3 = edge.field("in");
                    System.out.println(field3);

                }
                String fieldName = edgeName.replace("out_", "");
                String getterName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                try {
                    Object invoked = bean.getClass().getMethod(getterName).invoke(bean);
                    System.out.println(invoked);
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                ORecord load = document.load();
//
//                System.out.println(load);
                // OrientVertex vertexFromEdge = (OrientVertex)
                // e.getVertex(Direction.IN);
                // try {
                // Class<?> vertexClass =
                // getRegisteredClass(vertexFromEdge.getRecord().getClassName());
                // Identifiable beanFromVertex = vertexToBean(vertexFromEdge,
                // vertexClass);
                // Class<?> fieldType =
                // bean.getClass().getDeclaredField(edgeName).getType();
                // if (Collection.class.isAssignableFrom(fieldType)) {
                // Method collectionGetter = bean.getClass().getMethod(
                // "get" + edgeName.substring(0, 1).toUpperCase() +
                // edgeName.substring(1));
                // Collection<Identifiable> collection =
                // (Collection<Identifiable>) collectionGetter.invoke(bean);
                // if (collection == null) {
                // throw new IllegalStateException(
                // "could not add to collection object; please make sure your beans field '"
                // + edgeName + "' is initialized");
                // }
                // collection.add(beanFromVertex);
                // }
                // } catch (Exception e1) {
                // log.error(e1.getMessage(), e1);
                // }

                });
            return bean;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> List<T> findWithGraph(String sql, Class<?> cls, Map<String, Object> params) {
        OrientGraph db = getGraphDb();
        List<T> result = new ArrayList<>();
        Iterable<Vertex> query = (Iterable<Vertex>) db.getVerticesOfClass("Page");
        for (Vertex vertex : query) {
            OrientVertex ov = (OrientVertex) vertex;
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
            return (Long) field;
        }
        if (field instanceof Integer) {
            return new Long((Integer) field);
        }
        return 0;
    }

    @Override
    public Object executeUpdate(String sql, Map<String, Object> params) {
        OObjectDatabaseTx objectDb = getObjectDb();
        Object executed = objectDb.command(new OCommandSQL(sql)).execute(params);
        objectDb.commit();
        return executed;
    }

    @Override
    public Object executeUpdateVertex(String sql, Map<String, Object> params) {
        OrientGraph graphDb = getGraphDb();
        Object executed = graphDb.command(new OCommandSQL(sql)).execute(params);
        graphDb.commit();
        return executed;
    }

    @Override
    public void delete(Class<?> cls, String id) {
        OObjectDatabaseTx objectDb = getObjectDb();
        objectDb.getEntityManager().registerEntityClass(cls);
        ORecordId recordId = new ORecordId(id);
        Object loaded = objectDb.load(recordId);
        objectDb.delete(recordId);
    }

    @Override
    public void deleteVertex(String id) {
        OrientGraph db = getGraphDb();
        ORecordId recordId = new ORecordId(id);
        OrientVertex vertex = db.getVertex(recordId);
        db.removeVertex(vertex);
    }

    public synchronized void startDb() {
        if (started) {
            return;
        }
        try {
            log.info("about to start db");
            createDbIfNeeded();

            OPartitionedDatabasePool opDatabasePool = new OPartitionedDatabasePool(getDbUrl(), getDbUsername(),
                    getDbPassword());
            ODatabaseDocumentTx oDatabaseDocumentTx = opDatabasePool.acquire();
            OObjectDatabaseTx db = new OObjectDatabaseTx(oDatabaseDocumentTx);

            log.info("setting lazy loading to false");
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
        started = false;
    }

    private void createDbIfNeeded() {
        String dbUrl = getDbUrl();
        if (dbUrl.startsWith("remote")) {
            log.info("registering remote engine");
            Orient.instance().registerEngine(new OEngineRemote());
        } else if (dbUrl.startsWith("plocal")) {

            OrientGraph graph = new OrientGraph(dbUrl, getDbUsername(), getDbPassword());

            // final OrientGraphFactory factory = new OrientGraphFactory(dbUrl,
            // getDbUsername(), getDbPassword())
            // .setupPool(1, 10);
            try {
                log.info("testing graph factory connection");
                // graph.getTx();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                graph.shutdown();
            }
        } else if (dbUrl.startsWith("memory:")) {
            ODatabase<?> create = new OObjectDatabaseTx(dbUrl).create();
            log.info("created new in-memory database {}", create.toString());

            final OrientGraphFactory factory = new OrientGraphFactory(dbUrl, getDbUsername(), getDbPassword())
                    .setupPool(1, 10);
            try {
                log.info("testing graph factory connection");
                factory.getTx();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                factory.close();
            }
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
    public void createEdges(String... edges) {
        OObjectDatabaseTx objectDb = getObjectDb();
        try {
            Arrays.stream(edges).forEach(edge -> {
                if (objectDb.getMetadata().getSchema().getClass(edge) == null) {
                    OClass edgeClass = objectDb.getMetadata().getSchema().getClass("E");
                    objectDb.getMetadata().getSchema().createClass(edge).setSuperClass(edgeClass);
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
    public Class<?> getRegisteredClass(String classname) {
        OObjectDatabaseTx db = getObjectDb();
        try {
            return db.getEntityManager().getEntityClass(classname);
        } finally {
            db.close();
        }
    }

    @Override
    public void createUniqueIndex(Class<?> cls, String... fieldnames) {
        OObjectDatabaseTx db = getObjectDb();
        OClass oClass = db.getMetadata().getSchema().getClass(cls);

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

    private OrientGraph getGraphDb() {
        return graphDbFactory.getTx();
    }

    private ODatabaseDocumentTx getDocumentDb() {
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(getDbUrl()).open(getDbUsername(), getDbPassword());
        db.activateOnCurrentThread();
        return db;
    }

    public OObjectDatabaseTx getObjectDb() {
        OPartitionedDatabasePool opDatabasePool = new OPartitionedDatabasePool(getDbUrl(), getDbUsername(),
                getDbPassword());
        ODatabaseDocumentTx oDatabaseDocumentTx = opDatabasePool.acquire();
        return new OObjectDatabaseTx(oDatabaseDocumentTx);
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
        documentDb.save(doc);
        documentDb.commit();

    }

}

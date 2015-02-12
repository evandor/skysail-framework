package de.twenty11.skysail.server.db.orientdb;

import io.skysail.server.db.DbConfigurationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.iterator.OObjectIteratorClass;

import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.events.EventHandler;
import de.twenty11.skysail.server.events.SkysailEvents;
import de.twenty11.skysail.server.um.domain.SkysailRole;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Component(immediate = true)
@Slf4j
public class OrientDbService extends AbstractOrientDbService implements DbService {

    @Activate
    public void activate() {
        log.debug("activating {}", this.getClass().getName());

    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        log.debug("activating {}", this.getClass().getName());
        stopDb();
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=skysail)")
    public void setDbConfigurationProvider(DbConfigurationProvider provider) {
        updated(provider);
    }

    public void unsetDbConfigurationProvider(DbConfigurationProvider provider) {
    }

    @Override
    @Deprecated
    public void register(Class<?>... entities) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            Arrays.stream(entities).forEach(entity -> {
                db.getEntityManager().registerEntityClass(entity.getClass());
            });
        } finally {
            db.close();
        }
    }

    private void register(OObjectDatabaseTx db, Class<?>... registerClasses) {
        Arrays.stream(registerClasses).forEach(cls -> {
            db.getEntityManager().registerEntityClass(cls);
        });
    }

    private void register(OObjectDatabaseTx db, Class<?> entityClass, Class<?>... linkedClasses) {
        register(db, merge(entityClass, linkedClasses));
    }

    @Override
    public <T> T persist(T o, Class<?>... registerClasses) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            register(db, o.getClass(), registerClasses);
            ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
            db.getEntityManager().registerEntityClass(o.getClass());
            return db.save(o);
        } finally {
            db.close();
        }
    }

    @Override
    public <T> String getId(T o) {
        OObjectDatabaseTx db = getDbFromPool();
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        try {
            ODocument doc = db.getRecordByUserObject(o, false);
            return doc.getIdentity().toString();
        } finally {
            db.close();
        }
    }

    @Override
    public <T> T update(T o) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
            return db.save(o);
        } finally {
            db.close();
        }
    }

    @Override
    public <T> T find(String id, Class<?>... registerClasses) {
        ORID iRecordId = new ORecordId("#" + id);
        OObjectDatabaseTx db = getDbFromPool();
        register(db, registerClasses);
        try {
            T object = db.load(iRecordId);
            return object;
        } finally {
            db.close();
        }
    }

    @Override
    public String findAndReturnJson(String id, Class<?>... registerClasses) {
        ORID iRecordId = new ORecordId("#" + id);
        OObjectDatabaseTx db = getDbFromPool();
        register(db, registerClasses);
        try {
            Object object = db.load(iRecordId);
            ODocument doc = db.getRecordByUserObject(object, false);
            return doc.toJSON();
        } finally {
            db.close();
        }
    }

    @Override
    public <T> T findOne(String sql, Class<T> clz, Map<String, Object> params) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            db.getEntityManager().registerEntityClass(clz);
            OSQLSynchQuery<T> query = new OSQLSynchQuery<T>(sql);
            List<T> result = db.command(query).execute(params);
            if (result.isEmpty()) {
                throw new IllegalStateException("expected exactly one result for query, but resultset is empty");
            }
            if (result.size() > 1) {
                throw new IllegalStateException("expected exactly one result for query, but found " + result.size());
            }
            return result.get(0);
        } finally {
            db.close();
        }
    }

    @Override
    public <T> List<T> find(String sql, Class<T> clz, Map<String, Object> params) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            db.getEntityManager().registerEntityClass(clz);
            OSQLSynchQuery<T> query = new OSQLSynchQuery<T>(sql);
            return db.command(query).execute(params);
        } finally {
            db.close();
        }
    }

    @Override
    public void delete(String id) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            ORID iRecordId = new ORecordId("#" + id);
            db.delete(iRecordId);
        } finally {
            db.close();
        }
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass, Class<?>... linkedClasses) {
        OObjectDatabaseTx db = getDbFromPool();
        register(db, linkedClasses);
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        try {
            db.getEntityManager().registerEntityClass(entityClass);
            OObjectIteratorClass<T> iterator = db.browseClass(entityClass);

            int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED;
            Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
            Stream<T> stream = StreamSupport.stream(spliterator, false);
            return stream.collect(Collectors.toList());
        } finally {
            db.close();
        }
    }

    @Override
    public <T> List<T> query(OSQLSynchQuery<T> osqlSynchQuery, Class<?>... registerClasses) {
        OObjectDatabaseTx db = getDbFromPool();
        register(db, registerClasses);
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        try {
            // db.getEntityManager().registerEntityClass(entityClass);
            List<T> result = db.query(osqlSynchQuery);

            // int characteristics = Spliterator.DISTINCT | Spliterator.SORTED |
            // Spliterator.ORDERED;
            // Spliterator<T> spliterator =
            // Spliterators.spliteratorUnknownSize(iterator, characteristics);
            // Stream<T> stream = StreamSupport.stream(spliterator, false);
            return result;// stream.collect(Collectors.toList());
        } finally {
            db.close();
        }
    }

    @Override
    public <T> List<String> findAllAsJsonList(Class<T> entityClass, Class<?>... linkedClasses) {
        OObjectDatabaseTx db = getDbFromPool();
        register(db, linkedClasses);
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        List<String> result = new ArrayList<String>();
        try {
            db.getEntityManager().registerEntityClass(entityClass);
            OObjectIteratorClass<T> iterator = db.browseClass(entityClass);

            while (iterator.hasNext()) {
                T next = iterator.next();
                ODocument doc = db.getRecordByUserObject(next, false);
                result.add(doc.toJSON());
            }

            return result;
        } finally {
            db.close();
        }
    }

    @Override
    public <T> List<T> findAll(String sql, Class<T> entityClass, Map<String, Object> params) {
        OObjectDatabaseTx db = getDbFromPool();
        try {
            db.getEntityManager().registerEntityClass(entityClass);
            OSQLSynchQuery<T> query = new OSQLSynchQuery<T>(sql);
            List<T> result = db.command(query).execute(params);
            return result;
        } finally {
            db.close();
        }
    }

    protected synchronized void startDb() {
        if (started) {
            return;
        }
        try {
            createDbIfNeeded();
            OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
            db.setLazyLoading(false);
            registerDefaultClasses();
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

    private void registerDefaultClasses() {
        OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
        db.getEntityManager().registerEntityClass(SkysailUser.class);
        db.getEntityManager().registerEntityClass(SkysailRole.class);
        db.getEntityManager().registerEntityClass(DbVersion.class);
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
        List<DbVersion> versions = findAll(DbVersion.class);
        if (versions.size() == 0) {
            persist(new DbVersion(1l, "initial"));
            SkysailRole adminRole = new SkysailRole("admin");
            persist(adminRole);
            SkysailUser admin = new SkysailUser("admin", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
            admin.setRoles(Arrays.asList(adminRole));
            persist(admin);
        }
    }

    protected void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopDb();
            }
        });
    }

    private OObjectDatabaseTx getDbFromPool() {
        OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin", "admin");
        db.setLazyLoading(false);
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        return db;
    }

    private Class<?>[] merge(Class<?> entityClass, Class<?>[] entities) {
        List<Class<?>> list = new ArrayList<>();
        list.add(entityClass);
        list.addAll(Arrays.asList(entities));
        return list.toArray(new Class[list.size()]);
    }

}

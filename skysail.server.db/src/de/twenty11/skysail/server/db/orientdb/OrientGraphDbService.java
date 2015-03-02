package de.twenty11.skysail.server.db.orientdb;

import io.skysail.server.db.DbConfigurationProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONMode;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONUtility;

import de.twenty11.skysail.server.core.db.GraphDbService;
import de.twenty11.skysail.server.db.orientdb.impl.Persister;
import de.twenty11.skysail.server.db.orientdb.impl.Updater;
import de.twenty11.skysail.server.events.EventHandler;
import de.twenty11.skysail.server.events.SkysailEvents;

@Component(immediate = true)
@Slf4j
public class OrientGraphDbService extends AbstractOrientDbService implements GraphDbService {

    private OrientGraphFactory graphDbFactory;

    @Activate
    public void activate() {
        log.debug("activating {}", this.getClass().getName());
        graphDbFactory = new OrientGraphFactory(getDbUrl()).setupPool(1, 10);
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
    public <T> Object persist(T entity) {
        return new Persister(getDb()).persist(entity);
    }

    @Override
    public void update(JSONObject json) {
        new Updater(getDb()).update(json);
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
    public <T> JSONObject find(Class<?> cls, String id) {
        OrientGraph db = getDb();
        OrientVertex vertex = db.getVertex(id);
        try {
            return new GraphSONUtility(GraphSONMode.NORMAL, null).jsonFromElement(vertex);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    // @Override
    public <T> List<T> findAll(Class<T> entityClass, Class<?>... linkedClasses) {
        return null;
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

    private OrientGraph getDb() {
        return graphDbFactory.getTx();
    }

}

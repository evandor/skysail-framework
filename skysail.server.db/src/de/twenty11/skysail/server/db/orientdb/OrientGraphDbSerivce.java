package de.twenty11.skysail.server.db.orientdb;

import aQute.bnd.annotation.component.Component;

import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.Orient;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

import de.twenty11.skysail.server.core.db.GraphDbService;

@Component(immediate = true)
public class OrientGraphDbSerivce extends AbstractOrientDbService implements GraphDbService {

	private OrientGraph graph;
	private OrientGraphFactory factory;

	@Override
	protected void startDb() {
		if (started) {
			return;
		}
		try {
			createDbIfNeeded();
			// db = OObjectDatabasePool.global().acquire(getDbUrl(), "admin",
			// "admin");
			// db.setLazyLoading(false);
			// registerDefaultClasses();
			// initDbIfNeeded();
			started = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void registerShutdownHook() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void stopDb() {
		// TODO Auto-generated method stub

	}

	private void createDbIfNeeded() {
		if (getDbUrl().startsWith("remote")) {
			Orient.instance().registerEngine(new OEngineRemote());
		}
		factory = new OrientGraphFactory("memory:graphdb").setupPool(1, 10);

		// db = new OObjectDatabaseTx(getDbUrl());
		if (getDbUrl().startsWith("memory:")) {
			// sendEvent("GUI/alerts/warning",
			// "In-Memory database is being used, all data will be lost when application is shut down");
		}
		if (getDbUrl().startsWith("memory:") || getDbUrl().startsWith("plocal")) {
			// if (!db.exists()) {
			// db.create();
			// // importInitialData();
			// }
		}
	}

	public OrientGraph getGraph() {
		return factory.getTx();
	}

}

package de.twenty11.skysail.server.db.orientdb;

import io.skysail.server.db.DbConfigurationProvider;

import com.orientechnologies.orient.core.exception.ODatabaseException;

public abstract class AbstractOrientDbService {

    static final String DB_URL = "db.url";
    static final String DEFAULT_DB_URL = "memory:skysail";

    protected DbConfigurationProvider provider;
    protected boolean started = false;

    public void updated(DbConfigurationProvider provider) {
        this.provider = provider;
        if (provider == null || provider.getConfig() == null) {
            stopDb();
            return;
        }
        try {
            startDb();
            registerShutdownHook();
        } catch (ODatabaseException e) {
            if (!e.getMessage().startsWith(("Database instance has been released to the pool."))) {
                throw e;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            // updated(properties);
        }
    }

    protected abstract void registerShutdownHook();

    protected abstract void startDb();

    protected abstract void stopDb();

    protected String getDbUrl() {
        if (provider == null || provider.getConfig() == null) {
            return DEFAULT_DB_URL;
        }
        String url = provider.getConfig().getUrl();
        if (url != null) {
            return url;
        }
        return DEFAULT_DB_URL;
    }

}

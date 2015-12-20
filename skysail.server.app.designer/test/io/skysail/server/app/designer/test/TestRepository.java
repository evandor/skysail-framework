package io.skysail.server.app.designer.test;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbService;

public class TestRepository implements DbRepository {

    private DbService dbService;

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
    }

    @Override
    public Class<? extends Identifiable> getRootEntity() {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Object update(String id, Identifiable entity, String... edges) {
        return null;
    }

}

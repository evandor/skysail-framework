package io.skysail.server.app.designer.test;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
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
    public Object save(Identifiable identifiable, ApplicationModel appModel) {
        return null;
    }


    @Override
    public void delete(Identifiable identifiable) {
    }

    @Override
    public Object update(Identifiable entity, ApplicationModel applicationModel) {
        return null;
    }

}

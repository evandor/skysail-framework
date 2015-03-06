package io.skysail.server.app.crm.domain;

import io.skysail.server.app.crm.domain.companies.Company;

import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true)
public class CrmRepository<T> {

    private static CrmRepository instance;

    private DbService2 dbService;

    public static CrmRepository getInstance() {
        // for tests
        if (instance == null) {
            instance = new CrmRepository();
        }
        return instance;
    }

    @Activate
    public void activate() {
        CrmRepository.instance = this;
        dbService.setup();
    }

    @Deactivate
    public void deactivate() {
        CrmRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        this.dbService = null;
    }

    public List<T> findAll(Class<T> cls) {
        return dbService.findObjects(cls, "username");
    }

    public Object add(T entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public List<Map<String, Object>> getCompanys() {
        return dbService.getAllAsMap(Company.class, "username");
    }

}

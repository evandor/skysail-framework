package io.skysail.server.app.crm.domain;

import io.skysail.server.app.crm.domain.companies.Company;

import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true)
public class CrmRepository<T> {

    private static CrmRepository instance;

    private static DbService2 dbService;

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
        dbService.setupVertices(CrmEntity.class.getSimpleName(), DynamicEntity.class.getSimpleName());
    }

    @Deactivate
    public void deactivate() {
        CrmRepository.instance = null;
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        CrmRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        CrmRepository.dbService = null;
    }

    public static List<?> findAll(Class<?> cls) {
        return dbService.findObjects(cls, "username");
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public List<Map<String, Object>> getCompanys() {
        return dbService.getAllAsMap(Company.class, "username");
    }

}

package io.skysail.server.app.crm.domain;

import io.skysail.server.app.crm.domain.companies.Company;

import java.util.List;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.db.DbService2;

@Component(immediate = true)
public class CrmRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.setupVertices(CrmEntity.class.getSimpleName(), DynamicEntity.class.getSimpleName());
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        CrmRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        CrmRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return dbService.findObjects(cls, "username");
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public List<Map<String, Object>> getCompanys() {
        return dbService.getAllAsMap(Company.class, "username");
    }

}

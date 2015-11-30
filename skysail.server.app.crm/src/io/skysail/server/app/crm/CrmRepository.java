package io.skysail.server.app.crm;

import java.util.List;

import org.osgi.service.component.annotations.*;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.app.crm.contacts.Contact;
import io.skysail.server.db.DbService;

@Component(immediate = true, property = "name=CrmRepository")
public class CrmRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", CrmEntity.class.getSimpleName(), Company.class.getSimpleName(), Contact.class.getSimpleName());
        dbService.register(CrmEntity.class, Company.class, Contact.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        CrmRepository.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        CrmRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return dbService.findObjects("select from " + cls.getSimpleName());
    }

    public static Object add(Identifiable entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findById2(cls, id);
    }

    public Object update(String id, Identifiable entity, String... edges) {
        return dbService.update(id, entity);
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    @Override
    public Class<Identifiable> getRootEntity() {
        return null;
    }

}

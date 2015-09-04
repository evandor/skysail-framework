package io.skysail.app.propman;

import io.skysail.server.db.*;

import java.util.List;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=PropManRepository")
public class PropManRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() {
                dbService.createWithSuperClass("V", "Campaign");
        dbService.register(io.skysail.app.propman.Campaign.class);

    }

    @Reference
    public void setDbService(DbService dbService) {
        PropManRepository.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        PropManRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        return dbService.findObjects("select from " + cls.getSimpleName());
    }

    public <T> List<T> findAll(String sql) {
        return dbService.findObjects(sql);
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public void update(String id, Object entity) {
        dbService.update(id, entity);
    }

    public void register(Class<?>... classes) {
        dbService.register(classes);
    }

    public void delete(Class<?> cls, String id) {
        dbService.delete(cls, id);
    }

    public void createWithSuperClass(String superClassName, String entityClassName) {
        dbService.createWithSuperClass(superClassName, entityClassName);
    }


}

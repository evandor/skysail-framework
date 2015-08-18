package io.skysail.server.app.designer.repo;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.db.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=DesignerRepository")
@Slf4j
public class DesignerRepository implements DbRepository {

    private static DbService2 dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", Application.class.getSimpleName(), Entity.class.getSimpleName(),
                EntityField.class.getSimpleName(), ActionEntityField.class.getSimpleName());
        dbService.register(Application.class, Entity.class, EntityField.class, ActionEntityField.class);
        dbService.createEdges("entities", "fields");
    }

    @Reference
    public void setDbService(DbService2 dbService) {
        DesignerRepository.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        DesignerRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        try {
            return dbService.findObjects("select from " + cls.getSimpleName());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Collections.emptyList();
        }
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

    public Object update(Application entity, String... edges) {
        return dbService.update(entity.getId(), entity, edges);
    }

    public void update(Entity entity, String... edges) {
        dbService.update(entity.getId(), entity, edges);
    }

    public void update(EntityField field) {
        dbService.update(field.getId(), field);
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

    public Object getVertexById(Class<Application> cls, String id) {
        return dbService.findGraphs("SELECT FROM "+cls.getSimpleName()+" WHERE @rid="+id);
    }


}

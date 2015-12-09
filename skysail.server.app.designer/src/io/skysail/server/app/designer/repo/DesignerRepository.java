package io.skysail.server.app.designer.repo;

import java.util.*;

import org.osgi.service.component.annotations.*;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.db.*;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=DesignerRepository")
@Slf4j
public class DesignerRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(DbApplication.class), DbClassName.of(DbEntity.class),
                DbClassName.of(DbEntityField.class), DbClassName.of(ActionEntityField.class));
        dbService.register(DbApplication.class, DbEntity.class, DbEntityField.class, ActionEntityField.class);
        dbService.createEdges("entities", "fields");
    }

    @Reference
    public void setDbService(DbService dbService) {
        DesignerRepository.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        DesignerRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        try {

            return dbService.findGraphs(cls, "select from " + DbClassName.of(cls));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Collections.emptyList();
        }
    }

    public List<DbApplication> findApplications(String sql) {
        return dbService.findGraphs(DbApplication.class, sql);
    }

    public List<DbEntity> findEntities(String sql) {
        return dbService.findGraphs(DbEntity.class, sql);
    }

    public static Object add(Identifiable entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findById2(cls, id);
    }

    public Object update(DbApplication entity, String... edges) {
        return dbService.update(entity.getId(), entity, edges);
    }

    public Object update(DbEntity entity, String... edges) {
        return dbService.update(entity.getId(), entity, edges);
    }

    public void update(DbEntityField field) {
        dbService.update(field.getId(), field);
    }

    public Object update(String id, Identifiable entity, String... edges) {
        return dbService.update(id, entity, edges);
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

    public Object getVertexById(Class<DbApplication> cls, String id) {
        return dbService.findGraphs(cls.getClass(), "SELECT FROM "+cls.getSimpleName()+" WHERE @rid="+id);
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return dbService.findById2(DbApplication.class, id);
    }

    @Override
    public Class<Identifiable> getRootEntity() {
        return null;
    }


}

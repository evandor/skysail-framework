package io.skysail.server.app.designer.repo;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.relations.DbRelation;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=DesignerRepository")
@Slf4j
public class DesignerRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", 
                DbClassName.of(DbApplication.class), 
                DbClassName.of(DbEntity.class),
                DbClassName.of(DbRelation.class),
                DbClassName.of(DbEntityDateField.class), 
                DbClassName.of(DbEntityTextField.class),
                DbClassName.of(DbEntityTextareaField.class), 
                DbClassName.of(DbEntityTrixeditorField.class),
                DbClassName.of(DbEntityUrlField.class),
                DbClassName.of(ActionEntityField.class));
        dbService.register(
                DbApplication.class, 
                DbEntity.class, 
                DbRelation.class,
                DbEntityDateField.class, 
                DbEntityTextField.class,
                DbEntityTextareaField.class, 
                DbEntityTrixeditorField.class,
                DbEntityUrlField.class,
                ActionEntityField.class);
        dbService.createEdges("entities", "fields", "oneToManyRelations");
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
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<DbApplication> findApplications(String sql) {
        return dbService.findGraphs(DbApplication.class, sql);
    }

    public List<DbEntity> findEntities(String sql) {
        return dbService.findGraphs(DbEntity.class, sql);
    }

    public static OrientVertex add(Identifiable entity, ApplicationModel applicationModel) {
        return (OrientVertex) dbService.persist(entity, applicationModel);
    }

    public <T> T getById(Class<?> cls, String id) {
        return dbService.findById2(cls, id);
    }

    public void update(DbEntityField field, ApplicationModel applicationModel) {
        dbService.update(field, applicationModel);
    }

    public Object update(Identifiable entity, ApplicationModel applicationModel) {
        return dbService.update(entity, applicationModel);
    }


    public void register(Class<?>... classes) {
        dbService.register(classes);
    }

    public void delete(Class<?> cls, String id) {
        dbService.delete2(cls, id);
    }

    public void createWithSuperClass(String superClassName, String entityClassName) {
        dbService.createWithSuperClass(superClassName, entityClassName);
    }

    public Object getVertexById(Class<DbApplication> cls, String id) {
        return dbService.findGraphs(cls.getClass(), "SELECT FROM " + cls.getSimpleName() + " WHERE @rid=" + id);
    }

    @Override
    public Object save(Identifiable identifiable, ApplicationModel appModel) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return dbService.findById2(DbApplication.class, id);
    }

    public DbEntity findEntity(String id) {
        return dbService.findById2(DbEntity.class, id);
    }

    @Override
    public Class<Identifiable> getRootEntity() {
        return null;
    }

    @Override
    public void delete(Identifiable identifiable) {
        dbService.delete2(identifiable.getClass(), identifiable.getId());
    }

}

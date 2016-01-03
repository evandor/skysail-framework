package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.*;

public abstract class PutFieldResource<T extends DbEntityField> extends PutEntityServerResource<T> {

    private DesignerApplication app;
    private String entityId;
    private String fieldId;
    private DesignerRepository repo;

    @Override
    protected void doInit() {
        super.doInit();
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        fieldId = getAttribute(DesignerApplication.FIELD_ID);
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
    }

    @Override
    public T getEntity() {
        DbEntity entity = app.getEntity(entityId);
        Optional<DbEntityField> optionalField = entity.getFields().stream().filter(f -> f.getId().equals("#"+fieldId)).findFirst();
        if (optionalField.isPresent()) {
            return (T)optionalField.get();
        }
        return null;
    }

    @Override
    public void updateEntity(DbEntityField entity) {
        app.getRepository().update(entity);
    }

    @Override
    public List<TreeRepresentation> getTreeRepresentation() {
        DbEntity theEntity = repo.getById(DbEntity.class, getAttribute(DesignerApplication.ENTITY_ID));
        return app.getTreeRepresentation(theEntity.getDbApplication());
    }

}

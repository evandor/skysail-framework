package io.skysail.server.app.designer.fields.resources;

import java.util.Optional;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutFieldResource extends PutEntityServerResource<DbEntityField> {

    private DesignerApplication app;
    private String entityId;
    private String fieldId;

    @Override
    protected void doInit() {
        super.doInit();
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        fieldId = getAttribute(DesignerApplication.FIELD_ID);
        app = (DesignerApplication) getApplication();
    }

    @Override
    public DbEntityField getEntity() {
        DbEntity entity = app.getEntity(entityId);
        Optional<DbEntityField> optionalField = entity.getFields().stream().filter(f -> f.getId().equals("#"+fieldId)).findFirst();
        if (optionalField.isPresent()) {
            return optionalField.get();
        }
        return null;
    }

    @Override
    public SkysailResponse<DbEntityField> updateEntity(DbEntityField entity) {
        app.getRepository().update(entity);
        return new SkysailResponse<>();
    }

}

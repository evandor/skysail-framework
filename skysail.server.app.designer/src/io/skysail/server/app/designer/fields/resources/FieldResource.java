package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.restlet.resources.EntityServerResource;

public class FieldResource extends EntityServerResource<DbEntityField> {

    private String entityId;
    private String fieldId;
    private DesignerApplication app;

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
    public SkysailResponse<?> eraseEntity() {
        DbEntity dbEntity = app.getEntity(entityId);
        Optional<DbEntityField> optionalField = dbEntity.getFields().stream().filter(f -> f.getId().equals("#"+fieldId)).findFirst();
        if (optionalField.isPresent()) {
            Repository entityRepository = app.getRepository(DbEntity.class);
            dbEntity.getFields().stream().filter(f -> f.getId().equals("#"+fieldId)).findFirst().ifPresent(f -> {
//                dbEntity.getFields().remove(f);
//                entityRepository.save(dbEntity, app.getApplicationModel());
                entityRepository.delete(f);
            });
            return new SkysailResponse<>();
        } else {
            throw new IllegalStateException("field with id '#" + fieldId + "' does not exist.");
        }
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FieldsResource.class, PutFieldResource.class);
    }


}

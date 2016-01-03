package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.restlet.resources.EntityServerResource;

public class FieldResource extends EntityServerResource<DbEntityField> {

    private DesignerApplication app;

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
    }

    @Override
    public DbEntityField getEntity() {
        DbEntity entity = app.getEntity(getAttribute(DesignerApplication.ENTITY_ID));
        Optional<DbEntityField> optionalField = entity.getFields().stream().filter(f -> f.getId().equals("#"+getAttribute(DesignerApplication.FIELD_ID))).findFirst();
        if (optionalField.isPresent()) {
            return optionalField.get();
        }
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        DbEntity dbEntity = app.getEntity(getAttribute(DesignerApplication.ENTITY_ID));
        Optional<DbEntityField> optionalField = dbEntity.getFields().stream().filter(f -> f.getId().equals("#"+getAttribute(DesignerApplication.FIELD_ID))).findFirst();
        if (optionalField.isPresent()) {
            Repository entityRepository = app.getRepository(DbEntity.class);
            dbEntity.getFields().stream().filter(f -> f.getId().equals("#"+getAttribute(DesignerApplication.FIELD_ID))).findFirst().ifPresent(f -> {
                entityRepository.delete(f);
            });
            return new SkysailResponse<>();
        } else {
            throw new IllegalStateException("field with id '#" + getAttribute(DesignerApplication.FIELD_ID) + "' does not exist.");
        }
    }

    @Override
    public List<Link> getLinks() {
         List<Link> links = super.getLinks(PutFieldRedirectResource.class);
         links.stream().forEach(l -> l.setUri(l.getUri() + "?classHint={entity.type}"));
         return links;
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }


}

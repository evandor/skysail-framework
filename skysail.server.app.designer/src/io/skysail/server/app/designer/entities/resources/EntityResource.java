package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.resources.*;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;
import java.util.stream.Collectors;

public class EntityResource extends EntityServerResource<DbEntity> {

    private String appId;
    private String entityId;
    private DesignerApplication app;

    protected void doInit() {
        super.doInit();
        appId = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        app = (DesignerApplication) getApplication();
    }

    public DbEntity getEntity() {
        return app.getRepository().getById(DbEntity.class, appId);
//        return app.getEntity(application, entityId);
    }

    public List<Link> getLinks() {
        return super.getLinks(PutEntityResource.class, EntityResource.class, PostFieldResource.class,
                FieldsResource.class, PostSubEntityResource.class, SubEntitiesResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // app.invalidateMenuCache();
        DbApplication application = app.getRepository().getById(DbApplication.class, appId);
        application.setEntities(application.getEntities().stream().filter(e -> {
            return false;//!e.getId().equals("#" + entityId);
        }).collect(Collectors.toList()));
        app.getRepository().update(application);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        // TODO Auto-generated method stub
        return super.redirectTo(ApplicationsResource.class);
    }

}

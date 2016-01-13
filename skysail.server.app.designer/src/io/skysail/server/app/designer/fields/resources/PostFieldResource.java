package io.skysail.server.app.designer.fields.resources;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.forms.Tab;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.TreeRepresentation;

public abstract class PostFieldResource<T extends DbEntityField> extends PostEntityServerResource<T> {

    private DesignerApplication app;
    private DesignerRepository repo;

    public PostFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new DbEntityField");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
    }

    @Override
    public void addEntity(DbEntityField field) {
        DbEntity theEntity = repo.getById(DbEntity.class, getAttribute(DesignerApplication.ENTITY_ID));
        theEntity.getFields().add(field);
        app.getRepository().update(theEntity, ((DesignerApplication)getApplication()).getApplicationModel());
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

    @Override
    public List<TreeRepresentation> getTreeRepresentation() {
        DbEntity theEntity = repo.getById(DbEntity.class, getAttribute(DesignerApplication.ENTITY_ID));
        return app.getTreeRepresentation(theEntity.getDbApplication());
    }
    
    @Override
    public List<Tab> getTabs() {
        return super.getTabs(new Tab("newField","new Field",1), new Tab("optional","optional",2));
    }

}

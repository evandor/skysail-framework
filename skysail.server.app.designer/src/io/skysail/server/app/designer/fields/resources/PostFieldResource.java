package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.forms.Tab;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.Getter;

/**
 * see DesignerRepository
 * see FieldsResource
 */
public abstract class PostFieldResource<T extends DbEntityField> extends PostEntityServerResource<T> {

    private DesignerApplication app;
    private DesignerRepository repo;
    
    @Getter
    private static Set<Class<? extends PostFieldResource<?>>> extendingClasses = new HashSet<>();

    @Getter
    private List<FieldRole> fieldRoles;

    public PostFieldResource(Class<? extends PostFieldResource<?>> cls, FieldRole... fieldRoles) {
        this.fieldRoles = Arrays.asList(fieldRoles);
        addToContext(ResourceContextId.LINK_TITLE, "create new DbEntityField");
        extendingClasses.add(cls);
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
    public List<TreeStructure> getTreeRepresentation() {
        DbEntity theEntity = repo.getById(DbEntity.class, getAttribute(DesignerApplication.ENTITY_ID));
        return app.getTreeRepresentation(theEntity.getDbApplication());
    }
    
    @Override
    public List<Tab> getTabs() {
        int i = 1;
        return super.getTabs(new Tab("newField","new Field",i++), new Tab("visibility","Visibility",i++), new Tab("special","special Role",i++), new Tab("optional","optional",i++));
    }

}

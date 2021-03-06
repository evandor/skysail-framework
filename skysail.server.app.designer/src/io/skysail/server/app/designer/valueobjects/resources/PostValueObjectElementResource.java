package io.skysail.server.app.designer.valueobjects.resources;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.FieldRole;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.app.designer.valueobjects.DbValueObjectElement;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.Getter;

public class PostValueObjectElementResource extends PostEntityServerResource<DbValueObjectElement> {

    
    private DesignerApplication app;
    private DesignerRepository repo;
    
    @Getter
    private static Set<Class<? extends PostFieldResource<?>>> extendingClasses = new HashSet<>();

    @Getter
    private List<FieldRole> fieldRoles;

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
    }

    @Override
    public DbValueObjectElement createEntityTemplate() {
        return new DbValueObjectElement();
    }
    
    @Override
    public void addEntity(DbValueObjectElement element) {
        DbValueObject valueObject = repo.getById(DbValueObject.class, getAttribute("id"));
        valueObject.getElements().add(element);
        app.getRepository().update(valueObject, app.getApplicationModel());
    }

}

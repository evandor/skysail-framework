package io.skysail.server.app.designer.valueobjects.resources;

import java.util.Optional;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostValueObjectsResource extends PostEntityServerResource<DbValueObject> {

    private DesignerApplication app;
    private DesignerRepository repo;

    public PostValueObjectsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new DbValueObject");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbValueObject.class);
    }

    @Override
    public DbValueObject createEntityTemplate() {
        return new DbValueObject();
    }

    @Override
    public void addEntity(DbValueObject valueObject) {
        DbApplication dbApplication = (DbApplication) repo.findOne(getAttribute("id"));
        valueObject.setDbApplication(dbApplication);

        Optional<DbValueObject> existingValueObjectWithSameName = dbApplication.getValueObjects().stream()
                .filter(e -> e.getName().equals(valueObject.getName())).findFirst();
        if (existingValueObjectWithSameName.isPresent()) {
            throw new IllegalStateException("entity with same name already exists");
        }

        dbApplication.getValueObjects().add(valueObject);
        repo.update(dbApplication, app.getApplicationModel());

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

}

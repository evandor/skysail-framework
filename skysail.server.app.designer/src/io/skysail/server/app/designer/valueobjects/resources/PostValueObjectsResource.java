package io.skysail.server.app.designer.valueobjects.resources;

import java.util.Optional;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.designer.valueobjects.ValueObject;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostValueObjectsResource extends PostEntityServerResource<ValueObject> {

    private DesignerApplication app;
    private DesignerRepository repo;

    public PostValueObjectsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ValueObject");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(ValueObject.class);
    }

    @Override
    public ValueObject createEntityTemplate() {
        return new ValueObject();
    }

    @Override
    public void addEntity(ValueObject valueObject) {
        DbApplication dbApplication = (DbApplication) repo.findOne(getAttribute("id"));
        valueObject.setDbApplication(dbApplication);

        Optional<ValueObject> existingValueObjectWithSameName = dbApplication.getValueObjects().stream()
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

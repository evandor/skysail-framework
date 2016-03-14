package io.skysail.server.app.designer.valueobjects;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostValueObjectsResource extends PostEntityServerResource<ValueObject>{

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
    public void addEntity(ValueObject entity) {
        repo.save(entity, getApplication().getApplicationModel());
    }

}

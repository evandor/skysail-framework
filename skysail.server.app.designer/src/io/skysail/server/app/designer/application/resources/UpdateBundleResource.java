package io.skysail.server.app.designer.application.resources;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.codegen.Empty;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class UpdateBundleResource extends PostEntityServerResource<Empty> {

    private DesignerApplication app;

    public UpdateBundleResource() {
        addToContext(ResourceContextId.LINK_TITLE, "refresh bundle");
        addToContext(ResourceContextId.LINK_GLYPH, "flash");
    }

    @Override
    protected void doInit() {
        app = (DesignerApplication)getApplication();
    }

    @Override
    public Empty createEntityTemplate() {
        return new Empty();
    }

    @Override
    public void addEntity(Empty entity) {
        app.updateBundle();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

}

package io.skysail.server.app.designer.codegen;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostCompilationResource extends PostEntityServerResource<Empty> {

    private DesignerApplication app;

    public PostCompilationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "compile applications");
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
        InMemoryJavaCompiler.reset();
        app.compileApplication(getAttribute("id"));
        //app.updateBundle();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

}

package io.skysail.server.app.designer.codegen;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostCompilationResource extends PostEntityServerResource<String> {

    private DesignerApplication app;

    public PostCompilationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "compile applications");
    }
 
    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication)getApplication();
    }
    
    @Override
    public String createEntityTemplate() {
        return "";
    }

    @Override
    public SkysailResponse<?> addEntity(String entity) {
        app.compileApplications();
        return new SkysailResponse<String>();
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

}
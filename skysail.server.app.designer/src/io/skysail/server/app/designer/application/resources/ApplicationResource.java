package io.skysail.server.app.designer.application.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ApplicationResource extends EntityServerResource<Application> {

    private String id;
    private DesignerApplication app;

    public ApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "show");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (DesignerApplication) getApplication();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        app.getRepository().delete(Application.class, id);
        return null;
    }

    @Override
    public Application getEntity() {
        return app.getRepository().getById(Application.class, id);
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PutApplicationResource.class, ApplicationResource.class, PostEntityResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            if (id != null) {
                l.substitute("id", id);
            }
        };
    }

    @Override
    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        return super.redirectTo(ApplicationsResource.class);
    }

}

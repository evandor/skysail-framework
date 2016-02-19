package io.skysail.server.app.oEService;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostOEResource extends PostEntityServerResource<io.skysail.server.app.oEService.OE> {

	protected OEServiceApplication app;

    public PostOEResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (OEServiceApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.oEService.OE createEntityTemplate() {
        return new OE();
    }

    @Override
    public void addEntity(io.skysail.server.app.oEService.OE entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.oEService.OE.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(OEsResource.class);
    }
}
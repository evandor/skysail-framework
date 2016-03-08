package io.skysail.server.app.oEService.oe.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.oEService.*;
import io.skysail.server.app.oEService.oe.*;

/**
 * generated from postResource.stg
 */
public class PostOEResourceGen extends PostEntityServerResource<io.skysail.server.app.oEService.oe.OE> {

	protected OEServiceApplication app;

    public PostOEResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (OEServiceApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.oEService.oe.OE createEntityTemplate() {
        return new OE();
    }

    @Override
    public void addEntity(io.skysail.server.app.oEService.oe.OE entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.oEService.oe.OE.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(OEsResourceGen.class);
    }
}
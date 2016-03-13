package io.skysail.server.app.loop.entry.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.loop.*;
import io.skysail.server.app.loop.entry.*;

/**
 * generated from postResource.stg
 */
public class PostEntryResourceGen extends PostEntityServerResource<io.skysail.server.app.loop.entry.Entry> {

	protected LoopApplication app;

    public PostEntryResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (LoopApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.loop.entry.Entry createEntityTemplate() {
        return new Entry();
    }

    @Override
    public void addEntity(io.skysail.server.app.loop.entry.Entry entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.loop.entry.Entry.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntrysResourceGen.class);
    }
}
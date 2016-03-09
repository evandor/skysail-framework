package io.skysail.server.app.loop.entry.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.loop.*;
import io.skysail.server.app.loop.entry.*;

/**
 * generated from putResource.stg
 */
public class PutEntryResourceGen extends PutEntityServerResource<io.skysail.server.app.loop.entry.Entry> {


    protected String id;
    protected LoopApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (LoopApplication)getApplication();
    }

    @Override
    public void updateEntity(Entry  entity) {
        io.skysail.server.app.loop.entry.Entry original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.loop.entry.Entry.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.loop.entry.Entry getEntity() {
        return (io.skysail.server.app.loop.entry.Entry)app.getRepository(io.skysail.server.app.loop.entry.Entry.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntrysResourceGen.class);
    }
}
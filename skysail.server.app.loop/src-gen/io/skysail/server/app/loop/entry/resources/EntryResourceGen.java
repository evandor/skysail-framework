package io.skysail.server.app.loop.entry.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.app.loop.*;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


/**
 * generated from entityResource.stg
 */
public class EntryResourceGen extends EntityServerResource<io.skysail.server.app.loop.entry.Entry> {

    private String id;
    private LoopApplication app;
    private EntryRepository repository;

    public EntryResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (LoopApplication) getApplication();
        repository = (EntryRepository) app.getRepository(io.skysail.server.app.loop.entry.Entry.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.loop.entry.Entry getEntity() {
        return (io.skysail.server.app.loop.entry.Entry)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutEntryResourceGen.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntrysResourceGen.class);
    }


}
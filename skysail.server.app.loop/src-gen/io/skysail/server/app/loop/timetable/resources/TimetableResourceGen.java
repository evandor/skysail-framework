package io.skysail.server.app.loop.timetable.resources;

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
public class TimetableResourceGen extends EntityServerResource<io.skysail.server.app.loop.timetable.Timetable> {

    private String id;
    private LoopApplication app;
    private TimetableRepository repository;

    public TimetableResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (LoopApplication) getApplication();
        repository = (TimetableRepository) app.getRepository(io.skysail.server.app.loop.timetable.Timetable.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.loop.timetable.Timetable getEntity() {
        return (io.skysail.server.app.loop.timetable.Timetable)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTimetableResourceGen.class,PostEntryResourceGen.class,EntrysResourceGen.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResourceGen.class);
    }


}
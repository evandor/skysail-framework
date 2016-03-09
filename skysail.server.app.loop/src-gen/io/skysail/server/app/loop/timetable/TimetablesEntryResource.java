package io.skysail.server.app.loop.timetable;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


/**
 * generated from targetRelationResource.stg
 */
public class TimetablesEntryResource extends EntityServerResource<Entry> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Entry getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesEntrysResource.class, PostTimetablesEntryRelationResource.class);
    }

}
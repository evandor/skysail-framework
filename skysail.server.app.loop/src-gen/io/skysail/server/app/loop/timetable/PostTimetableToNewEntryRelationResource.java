package io.skysail.server.app.loop.timetable;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.PostRelationResource2;
import io.skysail.server.app.loop.*;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostTimetableToNewEntryRelationResource extends PostRelationResource2<Entry> {

    private LoopApplicationGen app;
    private TimetableRepository repo;
    private String parentId;

    public PostTimetableToNewEntryRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (LoopApplication) getApplication();
        repo = (TimetableRepository) app.getRepository(io.skysail.server.app.loop.timetable.Timetable.class);
        parentId = getAttribute("id");
    }

    public Entry createEntityTemplate() {
        return new Entry();
    }

    @Override
    public void addEntity(Entry entity) {
        Timetable parent = repo.findOne(parentId);
        parent.getEntrys().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesEntrysResource.class, PostTimetableToNewEntryRelationResource.class);
    }
}
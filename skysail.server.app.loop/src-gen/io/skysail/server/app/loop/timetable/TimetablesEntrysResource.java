package io.skysail.server.app.loop.timetable;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.app.loop.*;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


/**
 * generated from relationResource.stg
 */
public class TimetablesEntrysResource extends ListServerResource<Entry> {

    private LoopApplicationGen app;
    private EntryRepository oeRepo;

    public TimetablesEntrysResource() {
        super(TimetableResourceGen.class);//, TimetablesTimetableResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (LoopApplication) getApplication();
        oeRepo = (EntryRepository) app.getRepository(Entry.class);
    }

    @Override
    public List<Entry> getEntity() {
        return (List<Entry>) oeRepo.execute(Entry.class, "select * from " + DbClassName.of(Entry.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesEntrysResource.class, PostTimetablesEntryRelationResource.class);
    }
}
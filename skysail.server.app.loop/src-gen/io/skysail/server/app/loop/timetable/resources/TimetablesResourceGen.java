package io.skysail.server.app.loop.timetable.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.loop.*;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


/**
 * generated from listResource.stg
 */
public class TimetablesResourceGen extends ListServerResource<io.skysail.server.app.loop.timetable.Timetable> {

    private LoopApplication app;
    private TimetableRepository repository;

    public TimetablesResourceGen() {
        super(TimetableResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Timetables");
    }

    public TimetablesResourceGen(Class<? extends TimetableResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (LoopApplication) getApplication();
        repository = (TimetableRepository) app.getRepository(io.skysail.server.app.loop.timetable.Timetable.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.loop.timetable.Timetable> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostTimetableResourceGen.class,TimetablesResourceGen.class,EntrysResourceGen.class);
    }
}
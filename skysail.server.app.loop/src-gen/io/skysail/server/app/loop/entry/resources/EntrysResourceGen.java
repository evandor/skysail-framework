package io.skysail.server.app.loop.entry.resources;

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
public class EntrysResourceGen extends ListServerResource<io.skysail.server.app.loop.entry.Entry> {

    private LoopApplication app;
    private EntryRepository repository;

    public EntrysResourceGen() {
        super(EntryResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Entrys");
    }

    public EntrysResourceGen(Class<? extends EntryResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (LoopApplication) getApplication();
        repository = (EntryRepository) app.getRepository(io.skysail.server.app.loop.entry.Entry.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.loop.entry.Entry> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostEntryResourceGen.class,TimetablesResourceGen.class,EntrysResourceGen.class);
    }
}
package io.skysail.server.app.loop.timetable;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

import io.skysail.server.app.loop.*;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


/**
 * generated from postRelationResource.stg
 */
public class PostTimetablesEntryRelationResource extends PostRelationResource<io.skysail.server.app.loop.timetable.Timetable, io.skysail.server.app.loop.entry.Entry> {

    private LoopApplicationGen app;
    private EntryRepository EntryRepo;
    private TimetableRepository TimetableRepo;

    public PostTimetablesEntryRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (LoopApplicationGen) getApplication();
        EntryRepo = (EntryRepository) app.getRepository(io.skysail.server.app.loop.entry.Entry.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<Entry> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), EntryRepo.count(filter));
        return EntryRepo.find(filter, pagination);
    }

    @Override
    protected List<Entry> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), EntryRepo.count(filter));
        return EntryRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<Entry> entities) {
        String id = getAttribute("id");
        io.skysail.server.app.loop.timetable.Timetable theUser = TimetableRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        EntryRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.app.loop.timetable.Timetable theUser, Entry e) {
        if (!theUser.getEntrys().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getEntrys().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
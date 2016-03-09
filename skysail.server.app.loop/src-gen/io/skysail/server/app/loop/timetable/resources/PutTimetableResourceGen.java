package io.skysail.server.app.loop.timetable.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.loop.*;
import io.skysail.server.app.loop.timetable.*;

/**
 * generated from putResource.stg
 */
public class PutTimetableResourceGen extends PutEntityServerResource<io.skysail.server.app.loop.timetable.Timetable> {


    protected String id;
    protected LoopApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (LoopApplication)getApplication();
    }

    @Override
    public void updateEntity(Timetable  entity) {
        io.skysail.server.app.loop.timetable.Timetable original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.loop.timetable.Timetable.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.loop.timetable.Timetable getEntity() {
        return (io.skysail.server.app.loop.timetable.Timetable)app.getRepository(io.skysail.server.app.loop.timetable.Timetable.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResourceGen.class);
    }
}
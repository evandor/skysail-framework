package io.skysail.server.app.timetables.timetable.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.timetables.*;
import io.skysail.server.app.timetables.timetable.*;

/**
 * generated from postResource.stg
 */
public class PostTimetableResourceGen extends PostEntityServerResource<io.skysail.server.app.timetables.timetable.Timetable> {

	protected TimetablesApplication app;

    public PostTimetableResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TimetablesApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.timetables.timetable.Timetable createEntityTemplate() {
        return new Timetable();
    }

    @Override
    public void addEntity(io.skysail.server.app.timetables.timetable.Timetable entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.timetables.timetable.Timetable.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResourceGen.class);
    }
}
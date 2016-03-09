package io.skysail.server.app.loop.timetable.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.loop.*;
import io.skysail.server.app.loop.timetable.*;

/**
 * generated from postResource.stg
 */
public class PostTimetableResourceGen extends PostEntityServerResource<io.skysail.server.app.loop.timetable.Timetable> {

	protected LoopApplication app;

    public PostTimetableResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (LoopApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.loop.timetable.Timetable createEntityTemplate() {
        return new Timetable();
    }

    @Override
    public void addEntity(io.skysail.server.app.loop.timetable.Timetable entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.loop.timetable.Timetable.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResourceGen.class);
    }
}
package io.skysail.server.app.timetables.course.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.timetables.*;
import io.skysail.server.app.timetables.course.*;

/**
 * generated from putResource.stg
 */
public class PutCourseResourceGen extends PutEntityServerResource<io.skysail.server.app.timetables.course.Course> {


    protected String id;
    protected TimetablesApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (TimetablesApplication)getApplication();
    }

    @Override
    public void updateEntity(Course  entity) {
        io.skysail.server.app.timetables.course.Course original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.timetables.course.Course.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.timetables.course.Course getEntity() {
        return (io.skysail.server.app.timetables.course.Course)app.getRepository(io.skysail.server.app.timetables.course.Course.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CoursesResourceGen.class);
    }
}
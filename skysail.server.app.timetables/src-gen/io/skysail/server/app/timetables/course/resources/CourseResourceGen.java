package io.skysail.server.app.timetables.course.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


/**
 * generated from entityResourceNonAggregate.stg
 */
public class CourseResourceGen extends EntityServerResource<io.skysail.server.app.timetables.course.Course> {

    private String id;
    private TimetablesApplication app;
    //private CourseRepository repository;

    public CourseResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (TimetablesApplication) getApplication();
       // repository = (CourseRepository) app.getRepository(io.skysail.server.app.timetables.course.Course.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        //repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.timetables.course.Course getEntity() {
        return (io.skysail.server.app.timetables.course.Course)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutCourseResourceGen.class);
    }

}
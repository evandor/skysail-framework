package io.skysail.server.app.timetables.timetable;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


/**
 * generated from targetRelationResource.stg
 */
public class TimetablesCourseResource extends EntityServerResource<Course> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Course getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetablesCourseRelationResource.class);
    }

}
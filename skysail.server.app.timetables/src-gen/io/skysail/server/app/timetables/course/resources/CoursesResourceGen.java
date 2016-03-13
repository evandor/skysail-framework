package io.skysail.server.app.timetables.course.resources;

import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;



/**
 * generated from listResourceNonAggregate.stg
 */
public class CoursesResourceGen extends ListServerResource<io.skysail.server.app.timetables.course.Course> {

    private TimetablesApplication app;

    public CoursesResourceGen() {
        super(CourseResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Courses");
    }

    @Override
    protected void doInit() {
        app = (TimetablesApplication) getApplication();
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Course.class) + " WHERE #" + getAttribute("id") + " IN in('pages')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(Course.class, sql);   
    }

    public List<Link> getLinks() {
       return super.getLinks(PostCourseResourceGen.class);
    }
}
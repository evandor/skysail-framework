package io.skysail.server.app.timetables.timetable;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.PostRelationResource2;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostTimetableToNewCourseRelationResource extends PostRelationResource2<Course> {

    private TimetablesApplicationGen app;
    private TimetableRepository repo;
    private String parentId;

    public PostTimetableToNewCourseRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (TimetablesApplication) getApplication();
        repo = (TimetableRepository) app.getRepository(io.skysail.server.app.timetables.timetable.Timetable.class);
        parentId = getAttribute("id");
    }

    public Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(Course entity) {
        Timetable parent = repo.findOne(parentId);
        parent.getCourses().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetableToNewCourseRelationResource.class);
    }
}
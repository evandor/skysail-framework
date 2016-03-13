package io.skysail.server.app.timetables.timetable;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


/**
 * generated from relationResource.stg
 */
public class TimetablesCoursesResource extends ListServerResource<Course> {

    private TimetablesApplicationGen app;
    private CourseRepository oeRepo;

    public TimetablesCoursesResource() {
        super(TimetableResourceGen.class);//, TimetablesTimetableResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (TimetablesApplication) getApplication();
        oeRepo = (CourseRepository) app.getRepository(Course.class);
    }

    @Override
    public List<Course> getEntity() {
        return (List<Course>) oeRepo.execute(Course.class, "select * from " + DbClassName.of(Course.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetablesCourseRelationResource.class);
    }
}
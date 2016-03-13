package io.skysail.server.app.timetables.course.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;



/**
 * generated from postResourceNonAggregate.stg
 */
public class PostCourseResourceGen extends PostEntityServerResource<io.skysail.server.app.timetables.course.Course> {

	private TimetablesApplication app;
    private Repository repository;

    public PostCourseResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TimetablesApplication) getApplication();
        repository = null;//app.getRepository(Space.class);
    }

    @Override
    public io.skysail.server.app.timetables.course.Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(io.skysail.server.app.timetables.course.Course entity) {
        Subject subject = SecurityUtils.getSubject();

        io.skysail.server.app.timetables.timetable.Timetable entityRoot = (io.skysail.server.app.timetables.timetable.Timetable) repository.findOne(getAttribute("id"));
        entityRoot.getCourses().add(entity);
        repository.update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CoursesResourceGen.class);
    }
}
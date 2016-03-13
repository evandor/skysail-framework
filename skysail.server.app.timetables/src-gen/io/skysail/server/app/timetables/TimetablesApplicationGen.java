package io.skysail.server.app.timetables;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class TimetablesApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Timetables";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public TimetablesApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/Timetables/{id}", io.skysail.server.app.timetables.timetable.resources.TimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/", io.skysail.server.app.timetables.timetable.resources.PostTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/{id}/", io.skysail.server.app.timetables.timetable.resources.PutTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables", io.skysail.server.app.timetables.timetable.resources.TimetablesResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.timetables.timetable.resources.TimetablesResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses", io.skysail.server.app.timetables.timetable.TimetablesCoursesResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses/", io.skysail.server.app.timetables.timetable.PostTimetableToNewCourseRelationResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}", io.skysail.server.app.timetables.timetable.TimetablesCourseResource.class));
        router.attach(new RouteBuilder("/Courses/{id}", io.skysail.server.app.timetables.course.resources.CourseResourceGen.class));
        router.attach(new RouteBuilder("/Courses/", io.skysail.server.app.timetables.course.resources.PostCourseResourceGen.class));
        router.attach(new RouteBuilder("/Courses/{id}/", io.skysail.server.app.timetables.course.resources.PutCourseResourceGen.class));
        router.attach(new RouteBuilder("/Courses", io.skysail.server.app.timetables.course.resources.CoursesResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
package io.skysail.server.app.timetables.timetable;

import java.util.List;

import io.skysail.server.app.timetables.TimetableRepository;
import io.skysail.server.app.timetables.TimetablesApplicationGen;
import io.skysail.server.app.timetables.course.Course;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.PostRelationResource;


/**
 * generated from postRelationResource.stg
 */
public class PostTimetablesCourseRelationResource extends PostRelationResource<io.skysail.server.app.timetables.timetable.Timetable, io.skysail.server.app.timetables.course.Course> {

    private TimetablesApplicationGen app;
   // private CourseRepository CourseRepo;
    private TimetableRepository TimetableRepo;

    public PostTimetablesCourseRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (TimetablesApplicationGen) getApplication();
     //   CourseRepo = (CourseRepository) app.getRepository(io.skysail.server.app.timetables.course.Course.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<Course> getEntity() {
        Filter filter = new Filter(getRequest());
       // Pagination pagination = new Pagination(getRequest(), getResponse(), CourseRepo.count(filter));
        return null;//CourseRepo.find(filter, pagination);
    }

    @Override
    protected List<Course> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        //Pagination pagination = new Pagination(getRequest(), getResponse(), CourseRepo.count(filter));
        return null;//CourseRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<Course> entities) {
        String id = getAttribute("id");
        io.skysail.server.app.timetables.timetable.Timetable theUser = TimetableRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        //CourseRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.app.timetables.timetable.Timetable theUser, Course e) {
        if (!theUser.getCourses().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getCourses().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}
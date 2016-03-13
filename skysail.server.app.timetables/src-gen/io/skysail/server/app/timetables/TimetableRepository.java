package io.skysail.server.app.timetables;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

/**
 * generated from repository.stg
 */
@Component(immediate = true, property = "name=TimetablesRepository")
public class TimetableRepository extends GraphDbRepository<io.skysail.server.app.timetables.timetable.Timetable> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.timetables.timetable.Timetable" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.timetables.timetable.Timetable.class));
        dbService.register(io.skysail.server.app.timetables.timetable.Timetable.class);
    }

}
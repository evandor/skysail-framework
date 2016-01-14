package io.skysail.server.app.whatAbout;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.*;

@Component(immediate = true, property = "name=EventsRepository")
public class EventRepository extends GraphDbRepository<io.skysail.server.app.whatAbout.Event> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        //log.debug("activating io.skysail.server.app.whatAbout.Event" Repository);
        dbService.createWithSuperClass("V", DbClassName.of(io.skysail.server.app.whatAbout.Event.class));
        dbService.register(Event.class);
    }

}
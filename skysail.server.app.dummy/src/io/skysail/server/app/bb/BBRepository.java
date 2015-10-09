package io.skysail.server.app.bb;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.bb.achievements.DummyAchievement;
import io.skysail.server.db.*;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=BodyboosterRepository")
public class BBRepository extends GraphDbRepository<DummyGoal> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() { // NO_UCD
        dbService.createWithSuperClass("V", DummyGoal.class.getSimpleName(), DummyAchievement.class.getSimpleName());
        dbService.register(DummyGoal.class, DummyAchievement.class);
    }
}

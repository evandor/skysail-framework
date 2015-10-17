package io.skysail.server.db.it.clip;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;

@Component(immediate = true)
public class ClipApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "ClipApp";

    private static ClipApplication instance;

    private DbRepository myRepository;

    public ClipApplication() {
        super(APP_NAME);
        instance = this;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=ClipRepository)")
    public void setRepository(DbRepository repo) {
        this.myRepository = (ClipRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.myRepository = null;
    }

    public ClipRepository getRepository() {
        return (ClipRepository) myRepository;
    }

    public static ClipApplication getInstance() {
        return instance;
    }
}

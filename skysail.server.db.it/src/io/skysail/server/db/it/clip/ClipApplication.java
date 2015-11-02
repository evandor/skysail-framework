package io.skysail.server.db.it.clip;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;

import java.util.Arrays;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;

@Component(immediate = true)
public class ClipApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "ClipApp";
    private static ClipApplication instance;

    public ClipApplication() {
        super(APP_NAME, new ApiVersion(1),Arrays.asList(Clip.class));
        instance = this;
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setRepositories(Repositories repos) {
       super.setRepositories(repos);
    }

    public void unsetRepositories(DbRepository repo) {
        super.setRepositories(null);
    }


    public static ClipApplication getInstance() {
        return instance;
    }
}

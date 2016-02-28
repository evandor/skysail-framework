package io.skysail.server.db.it.clip;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import io.skysail.domain.core.Repositories;
import lombok.Getter;

@Component(immediate = true)
public class ClipApplication extends SkysailApplication implements ApplicationProvider {
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    private static final String APP_NAME = "ClipApp";
    private static ClipApplication instance;

    public ClipApplication() {
        super(APP_NAME, new ApiVersion(1),Arrays.asList(Clip.class));
        instance = this;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
       super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }


    public static ClipApplication getInstance() {
        return instance;
    }
}

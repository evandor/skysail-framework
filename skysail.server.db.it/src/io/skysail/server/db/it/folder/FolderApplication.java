package io.skysail.server.db.it.folder;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import lombok.Getter;

@Component(immediate = true)
public class FolderApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "FolderApp";
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;


    private static FolderApplication instance;

    private DbRepository myRepository;

    public FolderApplication() {
        super(APP_NAME);
        instance = this;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=FolderRepository)")
    public void setRepository(DbRepository repo) {
        this.myRepository = (FolderRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.myRepository = null;
    }

    public FolderRepository getRepository() {
        return (FolderRepository) myRepository;
    }

    public static FolderApplication getInstance() {
        return instance;
    }
}

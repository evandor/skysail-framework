package io.skysail.server.db.it.folder;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;

@Component(immediate = true)
public class FolderApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "FolderApp";

    private static FolderApplication instance;

    private DbRepository myRepository;

    public FolderApplication() {
        super(APP_NAME);
        instance = this;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=FolderRepository)")
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

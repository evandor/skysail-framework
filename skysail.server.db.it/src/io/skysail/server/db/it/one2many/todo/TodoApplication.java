package io.skysail.server.db.it.one2many.todo;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;

@Component(immediate = true)
public class TodoApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "TodoApplication";

    private static TodoApplication instance;

    private DbRepository myRepository;

    public TodoApplication() {
        super(APP_NAME);
        instance = this;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=TodoRepository)")
    public void setRepository(DbRepository repo) {
        this.myRepository = (TodoRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.myRepository = null;
    }

    public TodoRepository getRepository() {
        return (TodoRepository) myRepository;
    }

    public static TodoApplication getInstance() {
        return instance;
    }
}

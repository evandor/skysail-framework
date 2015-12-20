package io.skysail.server.db.it.one2many.todo;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import lombok.Getter;

@Component(immediate = true)
public class TodoApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "TodoApplication";
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    private static TodoApplication instance;

    private DbRepository myRepository;

    public TodoApplication() {
        super(APP_NAME);
        instance = this;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=TodoRepository)")
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

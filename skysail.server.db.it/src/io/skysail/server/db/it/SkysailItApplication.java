package io.skysail.server.db.it;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component(immediate = true)
public class SkysailItApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "TestApp";

    private static SkysailItApplication instance;

    private DbRepository myRepository;

    public SkysailItApplication() {
        super(APP_NAME);
        instance = this;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=TestRepository)")
    public void setRepository(DbRepository repo) {
        this.myRepository = (TestRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.myRepository = null;
    }

    @Override
    protected void attach() {
        super.attach();
        // router.attach(new RouteBuilder("/clips", ClipsResource.class));
        router.attach(new RouteBuilder("/clips/", PostClipResource.class));
        // router.attach(new RouteBuilder("/clips/{id}", ClipResource.class));
        // router.attach(new RouteBuilder("/clips/{id}/",
        // PutClipResource.class));
    }

    public TestRepository getRepository() {
        return (TestRepository) myRepository;
    }

    public static SkysailItApplication getInstance() {
        return instance;
    }
}

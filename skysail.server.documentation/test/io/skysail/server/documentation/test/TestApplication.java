package io.skysail.server.documentation.test;

import io.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class TestApplication extends SkysailApplication {

    public TestApplication() {
        setName("testapp");
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/path/to/listResource", TestListResource.class));
        router.attach(new RouteBuilder("/path/to/entityResource", TestEntityResource.class));
//        router.attach(new RouteBuilder("/path/to/postResource", TestPostEntityResource.class));
        router.attach(new RouteBuilder("/path/to/putResource", TestPutEntityResource.class));
    }
}
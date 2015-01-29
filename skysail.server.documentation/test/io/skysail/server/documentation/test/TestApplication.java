package io.skysail.server.documentation.test;

import io.skysail.server.documentation.test.ApiResourceTest.TestResource;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class TestApplication extends SkysailApplication {

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/path1", TestResource.class));
    }
}
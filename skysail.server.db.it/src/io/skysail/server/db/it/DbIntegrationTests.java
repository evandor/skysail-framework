package io.skysail.server.db.it;

import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.db.DbService;
import io.skysail.server.db.it.support.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.Response;

import aQute.bnd.annotation.component.*;

/**
 * Integration test starting OSGi, including database and (not-mocked) resources
 * (but no Browser) to verify the correct behavior of the bean-to-vertex and
 * vertex-to-bean chains.
 */
@Component(immediate = true)
public class DbIntegrationTests extends IntegrationTests {

    protected static SkysailApplication skysailApplication;

    TestRequest request = new TestRequest();
    Response response = new TestResponse(request);

    protected static DbService dbService;

    @Reference
    public void setDbService(DbService dbService) {
        DbIntegrationTests.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        DbIntegrationTests.dbService = null;
    }

    protected void setupEntityResource(SkysailServerResource<?> r) {
        r.setApplication(skysailApplication);
        r.init(null, request, response);
    }

    public void setUp() {
        request.clearAttributes();
    }

}

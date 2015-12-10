package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.test.AbstractDesignerResourceTest;
import io.skysail.server.restlet.resources.SkysailServerResource;

public abstract class AbstractApplicationResourceTest extends AbstractDesignerResourceTest {

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<DbApplication> result, DbApplication app, Status status) {
        DbApplication dbApplication = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(status));
        assertThat(dbApplication.getName(),is(equalTo(app.getName())));
        assertThat(dbApplication.getPackageName(),is(equalTo(app.getPackageName())));
        assertThat(dbApplication.getPath(),is(equalTo(app.getPath())));
        assertThat(dbApplication.getProjectName(),is(equalTo(app.getProjectName())));
        assertThat(dbApplication.getOwner(),is("admin"));
    }
}

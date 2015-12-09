package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.test.AbstractDesignerResourceTest;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.data.Status;

public abstract class AbstractApplicationResourceTest extends AbstractDesignerResourceTest {

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<DbApplication> result, DbApplication app) {
        DbApplication entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(app.getName())));
        assertThat(entity.getPackageName(),is(equalTo(app.getPackageName())));
        assertThat(entity.getPath(),is(equalTo(app.getPath())));
        assertThat(entity.getProjectName(),is(equalTo(app.getProjectName())));
        assertThat(entity.getOwner(),is("admin"));
    }
}

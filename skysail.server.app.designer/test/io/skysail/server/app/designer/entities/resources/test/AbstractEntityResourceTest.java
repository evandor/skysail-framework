package io.skysail.server.app.designer.entities.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.test.AbstractDesignerResourceTest;
import io.skysail.server.restlet.resources.SkysailServerResource;

public abstract class AbstractEntityResourceTest extends AbstractDesignerResourceTest {

    protected DbApplication createApplication() {
        DbApplication anApplication = DbApplication.builder().name("Application_" + randomString())
                .packageName("pgkname").path("../").projectName("projectName").build();
        SkysailResponse<DbApplication> post = postApplicationResource.post(anApplication, JSON_VARIANT);
        assertThat(post.getResponse().getStatus(), is(Status.SUCCESS_CREATED));
        getAttributes().clear();

        return post.getEntity();
    }

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<DbEntity> result,
            DbEntity entity, Status status) {
        DbEntity dbEntity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(), is(status));
        assertThat(dbEntity, is(entity));
    }

}

package io.skysail.server.app.designer.entities.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.test.AbstractDesignerResourceTest;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.data.Status;

public abstract class AbstractEntityResourceTest extends AbstractDesignerResourceTest {

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<DbEntity> result, String name) {
        DbEntity entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
    }

}

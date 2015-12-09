package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;

public class ApplicationResourceTest extends AbstractApplicationResourceTest {

    @Test
    public void gets_list_representation() {
        DbApplication aList = createValidApplication();

        getAttributes().put("id", aList.getId());
        init(applicationResource);

        SkysailResponse<DbApplication> get = applicationResource.getEntity2(HTML_VARIANT);

        assertThat(responses.get(applicationResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.getEntity().getName(), is(equalTo(aList.getName())));
    }

    @Test
    public void deletes_list_resource_if_empty() {
        DbApplication aList = createValidApplication();

        setAttributes("id", aList.getId());
        init(applicationResource);

        applicationResource.deleteEntity(HTML_VARIANT);
        assertThat(responses.get(applicationResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));

        Object byId = repo.getById(DbApplication.class, aList.getId());
        assertThat(byId, is(nullValue()));
    }

}

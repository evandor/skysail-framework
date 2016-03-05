package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;

@Ignore // FIXME
public class ApplicationResourceTest extends AbstractApplicationResourceTest {

    private DbApplication dbApplication;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dbApplication = createValidApplication();
        getAttributes().put("id", dbApplication.getId());
        init(applicationResource);
    }
    
    @Test
    public void retrieves_existing_application() {
        SkysailResponse<DbApplication> get = applicationResource.getEntity2(HTML_VARIANT);
        assertThat(responses.get(applicationResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.getEntity().getName(), is(equalTo(dbApplication.getName())));
    }

    @Test
    public void deletes_existing_application() {
        applicationResource.deleteEntity(HTML_VARIANT);
        assertThat(responses.get(applicationResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(repo.getById(DbApplication.class, dbApplication.getId()), is(nullValue()));
    }

}

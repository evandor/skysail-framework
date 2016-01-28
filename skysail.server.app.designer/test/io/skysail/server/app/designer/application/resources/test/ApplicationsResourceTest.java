package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.server.app.designer.application.DbApplication;

public class ApplicationsResourceTest extends AbstractApplicationResourceTest {

    private DbApplication dbApplication;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dbApplication = createValidApplication();
        init(applicationsResource);
    }
    
    @Test
    public void list_of_applications_contains_created_application() {
        List<DbApplication> get = applicationsResource.getEntity();

        assertThat(responses.get(applicationsResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(), is(greaterThanOrEqualTo(1)));
        DbApplication theList = get.stream().filter(list -> list.getName().equals(dbApplication.getName())).findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(theList.getName(), is(equalTo(dbApplication.getName())));
    }
}

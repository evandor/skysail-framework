package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.server.app.designer.application.DbApplication;

public class ApplicationsResourceTest extends AbstractApplicationResourceTest {

    @Test
    public void Application_contains_created_todo_list() {
        DbApplication app1 = createValidApplication();

        init(applicationsResource);
        List<DbApplication> get = applicationsResource.getEntity();

        assertThat(responses.get(applicationsResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(), is(greaterThanOrEqualTo(1)));

        DbApplication theList = get.stream().filter(list -> list.getName().equals(app1.getName())).findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(theList.getName(), is(equalTo(app1.getName())));
    }
}

package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.application.Application;

import java.util.List;

import org.junit.Test;
import org.restlet.data.Status;

public class ApplicationsResourceTest extends AbstractApplicationResourceTest {

    @Test
    public void Application_contains_created_todo_list() {
        Application app1 = createApplication();

        init(applicationsResource);
        List<Application> get = applicationsResource.getEntity();

        assertThat(responses.get(applicationsResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(), is(greaterThanOrEqualTo(1)));

        Application theList = get.stream().filter(list -> list.getName().equals(app1.getName())).findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(theList.getName(), is(equalTo(app1.getName())));
    }
}

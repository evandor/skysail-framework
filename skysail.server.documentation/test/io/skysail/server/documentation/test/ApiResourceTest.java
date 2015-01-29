package io.skysail.server.documentation.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.server.documentation.ApiResource;
import io.skysail.server.documentation.SkysailDocumentationProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.apidoc.ApplicationApi;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ApiResourceTest {

    public class TestResource extends ListServerResource<String> {

        @Override
        public List<String> getData() {
            return Arrays.asList("hi");
        }

    }

    public class TestApplication extends SkysailApplication {

        @Override
        protected void attach() {
            super.attach();
            router.attach(new RouteBuilder("/path1", TestResource.class));
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private ApiResource resource;

    @Spy
    private TestApplication application;

    private Response response;
    private Request request;

    private ConcurrentMap<String, Object> concurrentMap;

    @Before
    public void setUp() throws Exception {
        request = Mockito.mock(Request.class);
        response = Mockito.mock(Response.class);

        Mockito.when(response.getRequest()).thenReturn(request);
        concurrentMap = new ConcurrentHashMap<>();
        Mockito.when(response.getAttributes()).thenReturn(concurrentMap);

        DocumentationProvider documentationProvider = new SkysailDocumentationProvider();

        application.setDocumentationProvider(documentationProvider);

        Context context = new Context();
        resource.init(context, request, response);
        application.setContext(context);
        application.createInboundRoot();

    }

    @Test
    public void returns_three_documentation_entities_plus_one_attached_entity() throws Exception {
        List<ApplicationApi> entities = resource.getEntities();
        assertThat(entities.size(), is(4));
    }

    @Test
    public void testname() throws Exception {
        List<ApplicationApi> entities = resource.getEntities();
        ApplicationApi appApi = entities.stream().filter(e -> {
            return e.getPath().equals("/path1");
        }).findFirst().orElseThrow(IllegalStateException::new);
        assertThat(appApi.getPath(), is(equalTo("/path1")));
    }

}

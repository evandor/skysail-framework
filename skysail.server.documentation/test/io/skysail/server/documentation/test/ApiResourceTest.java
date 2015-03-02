package io.skysail.server.documentation.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.server.documentation.ApiResource;
import io.skysail.server.documentation.EntityDescriptor;
import io.skysail.server.documentation.ResourceApi;
import io.skysail.server.documentation.SkysailDocumentationProvider;
import io.skysail.server.documentation.SupportedMethod;
import io.skysail.server.testsupport.ListServerResourceTestBase;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.data.Method;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ApiResourceTest extends ListServerResourceTestBase {

    @InjectMocks
    private ApiResource resource;

    @Spy
    private TestApplication application;

    private List<ResourceApi> apis;

    @Before
    public void setUp() {
        // super.setUp();
        Context context = new Context();
        resource.init(context, request, response);
        application.setContext(context);
        application.createInboundRoot();

        apis = resource.getEntities(null);
        DocumentationProvider documentationProvider = new SkysailDocumentationProvider();

        application.setDocumentationProvider(documentationProvider);
    }

    @Test
    public void returns_the_apis_of_the_four_attached_resources() throws Exception {
        assertThat(apis.size(), is(4));
    }

    @Test
    public void resourceApi_handles_listResources() {
        ResourceApi appApi = getResourceApi(apis, "testapp/path/to/listResource");
        assertThat(appApi.getPath(), is(equalTo("testapp/path/to/listResource")));
        assertThat(appApi.getDesc(), is(equalTo("API description of class 'TestListResource'")));
        assertThat(appApi.getTargetClassName(), is(equalTo("io.skysail.server.documentation.test.TestListResource")));

        EntityDescriptor entity = appApi.getEntity();
        assertThat(entity.getName(), is("List&lt;java.lang.String&gt;"));

        List<SupportedMethod> methods = appApi.getMethods();
        assertThat(methods.size(), is(1));
        assertMethod(methods.get(0), Method.GET, ListServerResource.class, "getEntities", null);
        // "lists the entities according");
    }

    @Test
    public void resourceApi_handles_entityResources() {
        ResourceApi appApi = getResourceApi(apis, "testapp/path/to/entityResource");
        assertThat(appApi.getPath(), is(equalTo("testapp/path/to/entityResource")));
        assertThat(appApi.getDesc(), is(equalTo("API description of class 'TestEntityResource'")));
        assertThat(appApi.getTargetClassName(), is(equalTo("io.skysail.server.documentation.test.TestEntityResource")));

        EntityDescriptor entity = appApi.getEntity();
        assertThat(entity.getName(), is("java.lang.String"));

        List<SupportedMethod> methods = appApi.getMethods();
        assertThat(methods.size(), is(4));
        assertMethod(methods.get(0), Method.DELETE, EntityServerResource.class, "deleteEntity", null);// "deletes the entity");
        assertMethod(methods.get(1), Method.GET, EntityServerResource.class, "getDeleteForm", null);// "form to delete");
        assertMethod(methods.get(2), Method.GET, EntityServerResource.class, "getEntity", null);// "retrieves the entity");
        assertMethod(methods.get(3), Method.GET, EntityServerResource.class, "getJson", null);// "as JSON");
    }

    @Test
    public void resourceApi_handles_postResources() {
        ResourceApi appApi = getResourceApi(apis, "testapp/path/to/postResource");
        assertThat(appApi.getPath(), is(equalTo("testapp/path/to/postResource")));
        assertThat(appApi.getDesc(), is(equalTo("API description of class 'TestPostEntityResource'")));
        assertThat(appApi.getTargetClassName(),
                is(equalTo("io.skysail.server.documentation.test.TestPostEntityResource")));

        EntityDescriptor entity = appApi.getEntity();
        assertThat(entity.getName(), is("java.lang.String"));

        List<SupportedMethod> methods = appApi.getMethods();
        assertThat(methods.size(), is(4));
        assertMethod(methods.get(0), Method.GET, PostEntityServerResource.class, "createForm", null);// "create an html form");
        assertMethod(methods.get(1), Method.GET, PostEntityServerResource.class, "getJson", null);// "as Json");
        assertMethod(methods.get(2), Method.POST, PostEntityServerResource.class, "post", null);// "generic POST for JSON");
        assertMethod(methods.get(3), Method.POST, PostEntityServerResource.class, "post", null);// "generic POST for x-www");
    }

    @Test
    public void resourceApi_handles_putResources() {
        ResourceApi appApi = getResourceApi(apis, "testapp/path/to/putResource");
        assertThat(appApi.getPath(), is(equalTo("testapp/path/to/putResource")));
        assertThat(appApi.getDesc(), is(equalTo("API description of class 'TestPutEntityResource'")));
        assertThat(appApi.getTargetClassName(),
                is(equalTo("io.skysail.server.documentation.test.TestPutEntityResource")));

        EntityDescriptor entity = appApi.getEntity();
        assertThat(entity.getName(), is("java.lang.String"));

        List<SupportedMethod> methods = appApi.getMethods();
        assertThat(methods.size(), is(4));
        assertMethod(methods.get(0), Method.GET, PutEntityServerResource.class, "createForm", null);// "create an html form");
        assertMethod(methods.get(1), Method.GET, PutEntityServerResource.class, "getJson", null);// "no implementation");
        assertMethod(methods.get(2), Method.PUT, PutEntityServerResource.class, "putEntity", null);// "generic PUT for JSON");
        assertMethod(methods.get(3), Method.PUT, PutEntityServerResource.class, "put", null);// "generic PUT for x-www");
    }

    private void assertMethod(SupportedMethod method, Method verb, Class<? extends ServerResource> cls,
            String methodName, String containedMsg) {
        assertThat(method.getHttpVerb(), is(equalTo(verb)));
        assertThat(method.getMethod().getDeclaringClass(), is(equalTo(cls)));
        assertThat(method.getMethod().getName(), is(equalTo(methodName)));
        if (containedMsg != null) {
            assertThat(method.getDesc(), is(containsString(containedMsg)));
        }
    }

    private ResourceApi getResourceApi(List<ResourceApi> entities, String path) {
        return entities.stream().filter(e -> {
            return e.getPath().equals(path);
        }).findFirst().orElseThrow(IllegalStateException::new);
    }

}

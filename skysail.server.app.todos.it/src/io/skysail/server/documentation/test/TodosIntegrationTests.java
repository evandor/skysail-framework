package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.client.testsupport.Client;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.ServiceReference;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

public class TodosIntegrationTests extends IntegrationTests {

    @Before
    public void setUp() throws Exception {
        client = new Client(getBaseUrl()).loginAs("admin", "skysail");
        form = new Form();
    }

    @Test
    public void documentationProvider_is_available() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = thisBundle.getBundleContext()
                .getServiceReferences(DocumentationProvider.class, null);
        assertTrue("service not available", serviceRefs.size() > 0);
    }

    @Test
    public void resourceMap_contains_apiResource() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = thisBundle.getBundleContext()
                .getServiceReferences(DocumentationProvider.class, null);
        DocumentationProvider documentationProvider = thisBundle.getBundleContext().getService(
                serviceRefs.iterator().next());
        Map<String, Class<? extends ServerResource>> resourceMap = documentationProvider.getResourceMap();
        assertTrue(resourceMap.size() > 0);
    }

    @Test
    @Ignore
    public void get_html_on_TodosResource_returns_200() throws Exception {
        Representation representation = client.setUrl("/Todos/Todos").get(MediaType.TEXT_HTML);
        assertTrue(representation.getMediaType().getName().equals("text/html"));
        assertTrue(client.getResponse().getStatus().getCode() == 200);
    }

    @Test
    @Ignore
    public void perfTest() throws Exception {
        form.add("title", "mytitle");
        for (int i = 0; i < 1; i++) {
            client.setUrl("/TodoGen/Todos/").post(form, MediaType.TEXT_HTML);
        }
        for (int i = 0; i < 1; i++) {
            client.setUrl("/TodoGen/Todos").get(MediaType.TEXT_HTML);
        }
    }

    @Test
    @Ignore
    public void get_json_on_TodosResource_returns_200() throws Exception {
        Representation representation = client.setUrl("/Todos/Todos").get(MediaType.APPLICATION_JSON);
        assertTrue(client.getResponse().getStatus().getCode() == 200);
        assertTrue(representation.getMediaType().getName().equals("application/json"));
        String text = representation.getText();
        assertTrue(text.startsWith("["));
        assertTrue(text.endsWith("]"));
    }

    @Test
    @Ignore
    public void post_html_with_missing_title_returns_badRequest() {
        form.add("title", "");
        thrown.expectMessage("Bad Request");
        client.setUrl("/Todos/Todos/").post(form, MediaType.TEXT_HTML);
    }

    @Test
    @Ignore
    public void post_json_with_missing_title_returns_badRequest() {
        thrown.expectMessage("Bad Request");
        form.add("title", "");
        client.setUrl("/Todos/Todos/").post(form, MediaType.APPLICATION_JSON);
    }

    @Test
    @Ignore
    public void post_html_on_TodosResource_returns_200() {
        form.add("title", "mytitle");
        client.setUrl("/Todos/Todos/").post(form, MediaType.TEXT_HTML);
        assertTrue(client.getResponse().getStatus().getCode() == 200);

    }

}

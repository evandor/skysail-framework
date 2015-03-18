package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.client.testsupport.Client;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

public class TodosIntegrationTests {

    private static final String HOST = "http://localhost";
    private static final String PORT = "2015";
    private static String credentials;

    private Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());
    private Form form;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Client client;

    @Before
    public void setUp() throws Exception {
        credentials = loginAs("admin", "syksail");
        client = new Client(getBaseUrl(), credentials);
        form = new Form();
    }

    private String loginAs(String username, String password) {
        ClientResource cr = new ClientResource(getBaseUrl() + "/_logout?targetUri=/");
        Representation representation = cr.get();
        try {
            System.out.println(representation.getText());
            cr = new ClientResource(getBaseUrl() + "/_login");
            cr.setFollowingRedirects(true);
            Form form = new Form();
            form.add("username", "admin");
            form.add("password", "skysail");
            // "username=admin&password=skysail&submitButt  on="
            Representation post = cr.post(form, MediaType.TEXT_HTML);
            // System.out.println(post.getText());
            // System.out.println(cr.getResponse().getHeaders());
            // System.out.println(cr.getRequest().getHeaders());
            // System.out.println(cr.getResponse().getStatus());
            return cr.getResponse().getCookieSettings().getFirstValue("Credentials");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    private String getBaseUrl() {
        return HOST + (PORT != null ? ":" + PORT : "");
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
    public void get_html_on_TodosResource_returns_200() throws Exception {
        Representation representation = client.setUrl("/TodoGen/Todos").get(MediaType.TEXT_HTML);
        assertTrue(representation.getMediaType().getName().equals("text/html"));
        assertTrue(client.getResponse().getStatus().getCode() == 200);
    }

    @Test
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
    public void get_json_on_TodosResource_returns_200() throws Exception {
        Representation representation = client.setUrl("/TodoGen/Todos").get(MediaType.APPLICATION_JSON);
        assertTrue(client.getResponse().getStatus().getCode() == 200);
        assertTrue(representation.getMediaType().getName().equals("application/json"));
        String text = representation.getText();
        assertTrue(text.startsWith("["));
        assertTrue(text.endsWith("]"));
    }

    @Test
    public void post_html_with_missing_title_returns_badRequest() {
        form.add("title", "");
        thrown.expectMessage("Bad Request");
        client.setUrl("/TodoGen/Todos/").post(form, MediaType.TEXT_HTML);
    }

    @Test
    public void post_json_with_missing_title_returns_badRequest() {
        thrown.expectMessage("Bad Request");
        form.add("title", "");
        client.setUrl("/TodoGen/Todos/").post(form, MediaType.APPLICATION_JSON);
    }

    @Test
    public void post_html_on_TodosResource_returns_200() {
        form.add("title", "mytitle");
        client.setUrl("/TodoGen/Todos/").post(form, MediaType.TEXT_HTML);
        assertTrue(client.getResponse().getStatus().getCode() == 200);

    }

}

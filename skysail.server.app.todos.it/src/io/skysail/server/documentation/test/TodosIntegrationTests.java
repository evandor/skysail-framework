package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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

    @Before
    public void setUp() throws Exception {
        credentials = loginAs("admin", "syksail");
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
            System.out.println(post.getText());
            System.out.println(cr.getResponse().getHeaders());
            System.out.println(cr.getRequest().getHeaders());
            System.out.println(cr.getResponse().getStatus());
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

    // @Test
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
    public void get_TodosResource_returns_200() throws Exception {
        ClientResource cr = new ClientResource(getBaseUrl() + "/TodoGen/Todos");
        cr.getCookies().add("Credentials", credentials);
        Representation representation = cr.get();
        System.out.println(representation.getText());
    }

    public static void main(String[] args) throws Exception {
        TodosIntegrationTests tests = new TodosIntegrationTests();
        credentials = tests.loginAs("admin", "skysail");
        tests.get_TodosResource_returns_200();
    }
}

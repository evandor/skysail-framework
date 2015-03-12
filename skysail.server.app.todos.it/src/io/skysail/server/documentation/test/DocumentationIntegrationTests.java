package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

public class DocumentationIntegrationTests { // extends
                                             // ListServerResourceTestBase {

    private static final String HOST = "http://localhost";
    private static final String PORT = "2015";

    private Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());// .getBundleContext();

    // @BeforeClass
    // public static void init() {
    // context
    //
    // }
    @Before
    public void setUp() throws Exception {
        loginAs("admin", "syksail");
    }

    private void loginAs(String username, String password) {
        ClientResource cr = new ClientResource(getBaseUrl() + "/_logout?targetUri=/");
        Representation representation = cr.get();
        try {
            System.out.println(representation.getText());
            cr = new ClientResource(getBaseUrl() + "/_login");
            Form form = new Form();
            form.add("username", "admin");
            form.add("password", "skysail");
            // "username=admin&password=skysail&submitButt  on="
            Representation post = cr.post(form, MediaType.TEXT_HTML);
            System.out.println(post.getText());
            System.out.println(cr.getResponse().getHeaders());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    public static void main(String[] args) {
        DocumentationIntegrationTests tests = new DocumentationIntegrationTests();
        tests.loginAs("admin", "skysail");
    }
}

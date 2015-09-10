package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;

import java.util.*;

import org.junit.*;
import org.osgi.framework.*;
import org.restlet.resource.ServerResource;

@Ignore
public class DocumentationIntegrationTests { // extends
                                             // ListServerResourceTestBase {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    @Test
    public void documentationProvider_is_available() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = context.getServiceReferences(
                DocumentationProvider.class, null);
        assertTrue("service not available", serviceRefs.size() > 0);
    }

    @Test
    public void resourceMap_contains_apiResource() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = context.getServiceReferences(
                DocumentationProvider.class, null);
        DocumentationProvider documentationProvider = context.getService(serviceRefs.iterator().next());
        Map<String, Class<? extends ServerResource>> resourceMap = documentationProvider.getResourceMap();
        assertTrue(resourceMap.size() > 0);
    }
}

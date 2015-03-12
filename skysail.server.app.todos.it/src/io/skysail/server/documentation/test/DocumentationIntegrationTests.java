package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.restlet.resource.ServerResource;

public class DocumentationIntegrationTests { // extends
                                             // ListServerResourceTestBase {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    @Before
    public void setUp() throws Exception {
        Collection<ServiceReference<ConfigurationAdmin>> serviceRefs = context.getServiceReferences(
                ConfigurationAdmin.class, null);
        ConfigurationAdmin configurationAdmin = context.getService(serviceRefs.iterator().next());
        // configurationAdmin.

    }

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
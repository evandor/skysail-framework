package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.restlet.resource.ServerResource;

public class DocumentationIntegrationTests {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    @Test
    public void documentationProvider_is_available() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = context.getServiceReferences(
                DocumentationProvider.class, null);
        assertTrue(serviceRefs.size() > 0);
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

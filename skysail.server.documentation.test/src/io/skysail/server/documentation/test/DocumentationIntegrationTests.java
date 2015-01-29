package io.skysail.server.documentation.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class DocumentationIntegrationTests {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    @Test
    public void documentationProvider_is_available() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = context.getServiceReferences(
                DocumentationProvider.class, null);
        assertTrue(serviceRefs.size() > 0);
        // assertThat(validatorServiceReferences.size(), is(1));
    }

    @Test
    @Ignore
    public void testName() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = context.getServiceReferences(
                DocumentationProvider.class, null);
        context.getService(serviceRefs.iterator().next());

    }

}

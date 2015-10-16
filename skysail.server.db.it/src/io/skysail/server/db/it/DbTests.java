package io.skysail.server.db.it;

import io.skysail.client.testsupport.IntegrationTests;

import org.junit.*;
import org.osgi.framework.Bundle;
import org.restlet.resource.ClientResource;

public class DbTests extends IntegrationTests {

    private ClientResource cr;
    private SkysailItApplication skysailApplication;

    @Before
    public void setUp() throws Exception {
        skysailApplication = new SkysailItApplication();
    }

    @Test
    public void test() {
        Bundle[] bundles = thisBundle.getBundleContext().getBundles();
        for (Bundle bundle : bundles) {
            System.out.println(bundle.getSymbolicName() + " - " + bundle.getVersion().toString());
        }
    }

}

package io.skysail.api.validation.hibernate.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.hibernate.OSGiServiceDiscoverer;

import org.junit.Test;

public class OSGiServiceDiscovererTest {

    @Test
    public void testName() throws Exception {
        OSGiServiceDiscoverer discoverer = new OSGiServiceDiscoverer();
        assertThat(discoverer.getValidationProviders().size(), is(1));
    }
}

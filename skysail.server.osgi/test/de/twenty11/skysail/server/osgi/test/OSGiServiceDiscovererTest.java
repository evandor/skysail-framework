package de.twenty11.skysail.server.osgi.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.validation.spi.ValidationProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.twenty11.skysail.server.osgi.OSGiServiceDiscoverer;

public class OSGiServiceDiscovererTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testName() throws Exception {
        OSGiServiceDiscoverer discoverer = new OSGiServiceDiscoverer();
        List<ValidationProvider<?>> validationProviders = discoverer.getValidationProviders();
        assertThat(validationProviders.size(), is(1));
    }
}

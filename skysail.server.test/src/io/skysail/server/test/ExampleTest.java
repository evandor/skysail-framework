package io.skysail.server.test;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.twenty11.skysail.server.app.ApplicationList;

public class ExampleTest {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    private ServiceReference[] allServiceReferences;

    @Before
    public void setUp() throws Exception {
        allServiceReferences = context.getAllServiceReferences(null, null);
    }

    //
    // @AfterClass
    // public static void stopFramework() {
    // try {
    // FrameworkUtil.getBundle(ExampleTest.class).getBundleContext().getBundles()[0].stop();
    // } catch (BundleException e) {
    // e.printStackTrace();
    // }
    // }

    @Test
    public void some_services_are_available() throws Exception {
        assertThat(allServiceReferences.length > 10, org.hamcrest.CoreMatchers.is(true));
    }

    @Test
    public void some_services_are_available2() throws Exception {
        getService(ApplicationList.class);
    }

    private void getService(Class<?> cls) {
        Object service = context.getService(allServiceReferences[0]);
        Arrays.stream(allServiceReferences).filter(sr -> {
            return true;
        }).findFirst().orElseThrow(IllegalStateException::new);
    }
}

package io.skysail.server.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.DbConfigurationProvider;
import io.skysail.server.db.DbConfigurations;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;

import de.twenty11.skysail.server.app.ApplicationList;
import de.twenty11.skysail.server.app.ApplicationListProvider;

public class ServerIntegrationTests {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    private ServiceReference[] allServiceReferences;

    @Before
    public void setUp() throws Exception {
        allServiceReferences = context.getServiceReferences(null, null);
    }

    @Test
    public void some_services_are_available() throws Exception {
        assertThat(allServiceReferences.length > 10, org.hamcrest.CoreMatchers.is(true));
    }

    @Test
    public void some_services_are_available2() throws Exception {
        ServiceReference reference = getServiceReference(ApplicationListProvider.class);
        ApplicationListProvider service = (ApplicationListProvider) context.getService(reference);
        assertThat(service instanceof ApplicationList, org.hamcrest.CoreMatchers.is(true));
    }

    @Test
    public void ConfigurationAdmin_is_available() throws Exception {
        ServiceReference configAdminReference = getServiceReference(ConfigurationAdmin.class);
        ConfigurationAdmin configAdmin = (ConfigurationAdmin) context.getService(configAdminReference);
        // Configuration dbConfiguration =
        // configAdmin.createFactoryConfiguration(DbConfigurations.class.getSimpleName()
        // + "-test");
        // dbConfiguration.update();
        assertThat(configAdmin, is(org.hamcrest.CoreMatchers.notNullValue()));
    }

    @Test
    public void DbConfigurationProvider_is_available() throws Exception {
        ServiceReference reference = getServiceReference(DbConfigurationProvider.class);
        DbConfigurationProvider service = (DbConfigurationProvider) context.getService(reference);
        assertThat(service instanceof DbConfigurations, org.hamcrest.CoreMatchers.is(true));
    }

    private ServiceReference getServiceReference(Class<?> cls) {
        return Arrays.stream(allServiceReferences).filter(sr -> {
            return implementsType(sr, cls);
        }).findFirst().orElseThrow(IllegalStateException::new);
    }

    private boolean implementsType(ServiceReference sr, Class<?> cls) {
        String[] implementsTypes = (String[]) sr.getProperty("objectClass");
        return Arrays.stream(implementsTypes).filter(type -> {
            System.out.println(type + "/" + cls.getName());
            return type.equals(cls.getName());
        }).findFirst().isPresent();
    }
}

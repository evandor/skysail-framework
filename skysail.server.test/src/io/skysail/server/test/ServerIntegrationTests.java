package io.skysail.server.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.DbConfig;
import io.skysail.server.db.DbConfigurationProvider;
import io.skysail.server.db.DbConfigurations;
import io.skysail.server.features.repositories.FeaturesRepository;

import java.util.Arrays;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;

import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.features.repository.StateRepository;
import de.twenty11.skysail.server.app.ApplicationList;
import de.twenty11.skysail.server.app.ApplicationListProvider;
import de.twenty11.skysail.server.core.restlet.SecurityFeatures;

@Slf4j
public class ServerIntegrationTests {

    private BundleContext context;

    @Before
    public void setUp() {
        log.warn("setting up integration test");
        Bundle bundle = FrameworkUtil.getBundle(this.getClass());
        log.warn("bundle was set to {}", bundle.getSymbolicName());
        context = bundle.getBundleContext();
    }

    // @Test
    // @Ignore
    // public void some_services_are_available() throws Exception {
    // ServiceReference[] serviceReferences = context.getServiceReferences(null,
    // null);
    // assertThat(serviceReferences.length > 10,
    // org.hamcrest.CoreMatchers.is(true));
    // }

    @Test
    @Ignore
    public void some_services_are_available2() throws Exception {
        ServiceReference reference = getServiceReference(ApplicationListProvider.class, null);
        ApplicationListProvider service = (ApplicationListProvider) context.getService(reference);
        assertThat(service instanceof ApplicationList, org.hamcrest.CoreMatchers.is(true));
    }

    @Test
    @Ignore
    public void ConfigurationAdmin_is_available() throws Exception {
        ServiceReference configAdminReference = getServiceReference(ConfigurationAdmin.class, null);
        ConfigurationAdmin configAdmin = (ConfigurationAdmin) context.getService(configAdminReference);
        // Configuration dbConfiguration =
        // configAdmin.createFactoryConfiguration(DbConfigurations.class.getSimpleName()
        // + "-test");
        // dbConfiguration.update();
        assertThat(configAdmin, is(org.hamcrest.CoreMatchers.notNullValue()));
    }

    @Test
    @Ignore
    public void DbConfigurationProvider_is_available() throws Exception {
        ServiceReference reference = getServiceReference(DbConfigurationProvider.class, "(name=defaultDbConfig)");
        DbConfigurationProvider service = (DbConfigurationProvider) context.getService(reference);
        assertThat(service instanceof DbConfigurations, org.hamcrest.CoreMatchers.is(true));

        DbConfig config = service.getConfig();
        assertThat(config.getDriver(), is(equalTo("thedriver")));
        assertThat(config.getUrl(), is(equalTo("theurl")));
        assertThat(config.getUsername(), is(equalTo("theusername")));
        assertThat(config.getPassword(), is(equalTo("thepassword")));
        assertThat(config.get("addProp"), is(equalTo("theAdditionalProperty")));
    }

    @Test
    @Ignore
    public void StateRepository_is_available() throws Exception {
        ServiceReference reference = getServiceReference(StateRepository.class, "(name=SecurityFeatures)");
        assertThat(reference, is(notNullValue()));
    }

    @Test
    @Ignore
    public void allow_origin_feature_is_active() throws Exception {
        ServiceReference reference = getServiceReference(StateRepository.class, "(name=SecurityFeatures)");
        assertThat(reference, is(notNullValue()));
        StateRepository service = (StateRepository) context.getService(reference);
        assertThat(service instanceof FeaturesRepository, org.hamcrest.CoreMatchers.is(true));

        FeatureState featureState = service.getFeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE);
        assertThat(featureState.isEnabled(), is(true));
    }

    private ServiceReference<?> getServiceReference(Class<?> cls, String filter) throws Exception {
        Collection<? extends ServiceReference<?>> serviceReferences = context.getServiceReferences(cls, filter);
        return serviceReferences.stream().filter(sr -> {
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

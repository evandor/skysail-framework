package de.twenty11.skysail.api.config.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.config.Configuration;
import de.twenty11.skysail.api.config.ConfigurationProvider;
import de.twenty11.skysail.api.internal.ApiConstants;

public class ConfigurationTest {

    private Configuration configuration;
    private ConfigurationProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = Mockito.mock(ConfigurationProvider.class);
        configuration = new Configuration();
    }

    @Test
    public void returns_empty_string_as_home_if_unconfigured() {
        configuration.setConfigurationProvider(null);
        assertThat(Configuration.getHome(), is(equalTo("")));
        // configuration.setConfigurationProvider(provider);
    }

    @Test
    public void standard_definition_is_returned_as_home() {
        Mockito.when(provider.getConfigForKey(ApiConstants.SKYSAIL_PRODUCT_HOME)).thenReturn("http://localhost:2016");
        configuration.setConfigurationProvider(provider);
        assertThat(Configuration.getHome(), is(equalTo("http://localhost:2016")));
    }

    @Test
    public void standard_definition_with_trailing_slash_is_returned_without_trailing_slash() {
        Mockito.when(provider.getConfigForKey(ApiConstants.SKYSAIL_PRODUCT_HOME)).thenReturn("http://localhost:2016/");
        configuration.setConfigurationProvider(provider);
        assertThat(Configuration.getHome(), is(equalTo("http://localhost:2016")));
    }

    @Test
    public void null_url_empty_string() {
        Mockito.when(provider.getConfigForKey(ApiConstants.SKYSAIL_PRODUCT_HOME)).thenReturn(null);
        configuration.setConfigurationProvider(provider);
        assertThat(Configuration.getHome(), is(equalTo("")));
    }

    @Test
    public void empty_url_empty_string() {
        Mockito.when(provider.getConfigForKey(ApiConstants.SKYSAIL_PRODUCT_HOME)).thenReturn("");
        configuration.setConfigurationProvider(provider);
        assertThat(Configuration.getHome(), is(equalTo("")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_valid_url_gives_exception() {
        Mockito.when(provider.getConfigForKey(ApiConstants.SKYSAIL_PRODUCT_HOME)).thenReturn("localhost:2016/");
        configuration.setConfigurationProvider(provider);
        Configuration.getHome();
    }

}

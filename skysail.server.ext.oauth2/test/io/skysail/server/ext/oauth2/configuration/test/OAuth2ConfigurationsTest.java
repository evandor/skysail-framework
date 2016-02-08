package io.skysail.server.ext.oauth2.configuration.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.skysail.server.ext.oauth2.configuration.OAuth2Config;
import io.skysail.server.ext.oauth2.configuration.OAuth2Configurations;

public class OAuth2ConfigurationsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private OAuth2Configurations oAuth2Configurations;
    private Map<String, String> config = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        oAuth2Configurations = new OAuth2Configurations();
    }
    
    @Test
    public void null_config_yields_exception() {
        thrown.expect(NullPointerException.class);
        oAuth2Configurations.activate(null);
    }

    @Test
    public void valid_configuration_is_added_to_map() {
        setConfig("facebook", "cid", "csecret","redirect");
        
        oAuth2Configurations.activate(config);
        
        OAuth2Config facebookConfig = oAuth2Configurations.get("facebook");
        assertThat(facebookConfig, is(notNullValue()));
        assertThat(facebookConfig.getClientId(),is("cid"));
        assertThat(facebookConfig.getClientSecret(),is("csecret"));
        assertThat(facebookConfig.getRedirectUrl(),is("redirect"));
        
    }

    @Test
    public void missing_name_yields_exception() {
        thrown.expect(IllegalArgumentException.class);
        setConfig("", "cid", "csecret", "redirect");
        oAuth2Configurations.activate(config);
    }

    @Test
    public void missing_clientId_yields_exception() {
        thrown.expect(IllegalArgumentException.class);
        setConfig("facebook", "", "csecret", "redirect");
        oAuth2Configurations.activate(config);
    }

    @Test
    public void adding_config_with_same_name_yields_exception() {
        thrown.expect(IllegalStateException.class);
        setConfig("facebook", "cid", "csecret", "redirect");
        oAuth2Configurations.activate(config);
        oAuth2Configurations.activate(config);
    }

    private void setConfig(String name, String cid, String csecret, String redirectUrl) {
        config.put("name",name);
        config.put("clientId",cid);
        config.put("clientSecret",csecret);
        config.put("redirectUrl", redirectUrl);
    }

}

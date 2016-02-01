package io.skysail.server.ext.oauth2.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import lombok.NonNull;

/**
 * collections configurations for oAuth2 client definitions provided in files with the name pattern
 * 'oAuth2-&lt;aName&gt;.cfg.
 * 
 * The value of "aName" is ignored; the identifier for a specific configuration is the one defined in
 * the property "name" inside the file.
 * 
 * Example file:
 * <code><pre>
 * name=facebook
 * clientId=...
 * clientSecret=...
 * </pre></code>
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = "oAuth2", service = OAuth2Configurations.class)
public class OAuth2Configurations {

    private static final String NAME = "name";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String REDIRECT_URL = "redirectUrl";
    
    private static Map<String, OAuth2Config> oAuth2Configs = new HashMap<>();

    @Activate
    public synchronized void activate(@NonNull Map<String, String> config) {
        validate(config);
        add(config);
    }

    @Deactivate
    public void deactivate(Map<String, String> config) {
        oAuth2Configs = new HashMap<>();
    }

    public OAuth2Config get(String identifier) {
        return oAuth2Configs.get(identifier);
    }

    private void add(Map<String, String> config) {
        String name = config.get(NAME);
        oAuth2Configs.put(name, new OAuth2Config(name,config.get(CLIENT_ID),config.get(CLIENT_SECRET),config.get(REDIRECT_URL)));
    }

    private void validate(Map<String, String> config) {
        checkProperty(NAME, config);
        checkProperty(CLIENT_ID, config);
        String configName = config.get(NAME);
        OAuth2Config existingConfiguration = oAuth2Configs.get(configName);
        if (existingConfiguration != null) {
            throw new IllegalStateException("OAuth2 Configuration for '"+configName+"' already exists");
        }
    }

    private void checkProperty(String propertyName, Map<String, String> config) {
        if (StringUtils.isEmpty(config.get(propertyName))) {
            throw new IllegalArgumentException("name must not be null");
        }
    }


}

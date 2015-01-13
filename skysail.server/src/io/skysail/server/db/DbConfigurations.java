package io.skysail.server.db;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(configurationPolicy = ConfigurationPolicy.REQUIRE)
public class DbConfigurations implements DbConfigurationProvider {

    @Activate
    public void activate(Map<String, String> config) {
        String url = config.get("url");
    }
}
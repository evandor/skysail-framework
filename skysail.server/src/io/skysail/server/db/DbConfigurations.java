package io.skysail.server.db;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class DbConfigurations implements DbConfigurationProvider {

    private DbConfig dbConfig;

    @Activate
    public void activate(Map<String, String> config) {
        dbConfig = new DbConfig(config);
        log.info("activating {} with config {}", this.getClass().getSimpleName(), dbConfig);
    }

    @Override
    public DbConfig getConfig() {
        return dbConfig;
    }
}
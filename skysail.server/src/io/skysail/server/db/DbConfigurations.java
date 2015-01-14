package io.skysail.server.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class DbConfigurations implements DbConfigurationProvider {

    private Map<String, DbConfig> dbConfigs = new ConcurrentHashMap<>();

    @Activate
    public void activate(Map<String, String> config) {
        @NonNull
        String name = config.get(DbConfig.NAME);
        DbConfig dbConfig = new DbConfig(config);
        log.info("activating {} with config {}", this.getClass().getSimpleName(), dbConfig);
        dbConfigs.put(name, dbConfig);
    }

    @Override
    public DbConfig getConfig(String name) {
        return dbConfigs.get(name);
    }
}
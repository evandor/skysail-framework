package io.skysail.server.db;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Constants;
import org.osgi.service.cm.*;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.optional)
@Slf4j
public class DbConfigurations implements DbConfigurationProvider {

    private DbConfig dbConfig;
    private ConfigurationAdmin configurationAdmin;
    private Thread loggerThread;

    @Activate
    public void activate(Map<String, String> config) {
        if (config.get(Constants.SERVICE_PID) == null) {
            scheduleCreationOfDefaultConfiguration();
            return;
        }
        dbConfig = new DbConfig(config);
        log.info("activating {} with config {}", this.getClass().getSimpleName(), dbConfig);
    }

    private void scheduleCreationOfDefaultConfiguration() {
        Runnable runnable = () -> {
            createDefaultConfigAfterWaiting(5000);
        };
        loggerThread = new Thread(runnable);
        loggerThread.start();
    }

    private void createDefaultConfigAfterWaiting(int ms) {
        try {
            Thread.sleep(ms);

            log.warn("no default database configuration was provided; creating a new one...");
            String instancePid = configurationAdmin.createFactoryConfiguration(DbConfigurations.class.getName(), null)
                    .getPid();
            Configuration config = configurationAdmin.getConfiguration(instancePid);
            Dictionary<String, Object> props = config.getProperties();
            if (props == null) {
                props = new Hashtable<String, Object>();
            }
            props.put("name", "skysailgraph");
            props.put("url", "memory:skysail");
            props.put("username", "admin");
            props.put("password", "admin");
            config.update(props);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

    }

    // --- ConfigurationAdmin ------------------------------------------------

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = null;
    }

    @Override
    public DbConfig getConfig() {
        return dbConfig;
    }
}
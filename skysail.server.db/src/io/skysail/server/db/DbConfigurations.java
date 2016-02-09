package io.skysail.server.db;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import lombok.extern.slf4j.Slf4j;

//@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, configurationPid = "db")
@Slf4j
public class DbConfigurations implements DbConfigurationProvider {

    private DbConfig dbConfig;
    
    @Reference
    private ConfigurationAdmin configurationAdmin;
    
    private Thread loggerThread;

    @Activate
    public void activate(Map<String, String> config) {
        if (config.get(Constants.SERVICE_PID) == null) {
            scheduleCreationOfDefaultConfiguration();
            return;
        }
        dbConfig = new DbConfig(config);
        log.debug("activating {} with config {}", this.getClass().getSimpleName(), dbConfig);
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

//    @Reference(cardinality = ReferenceCardinality.MANDATORY)//        dynamic = true, optional = false, multiple = false)
//    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
//        this.configurationAdmin = configurationAdmin;
//    }
//
//    public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
//        this.configurationAdmin = null;
//    }

    @Override
    public DbConfig getConfig() {
        return dbConfig;
    }
}
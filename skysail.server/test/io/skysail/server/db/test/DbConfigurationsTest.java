package io.skysail.server.db.test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.DbConfig;
import io.skysail.server.db.DbConfigurations;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Constants;

public class DbConfigurationsTest {

    private DbConfigurations dbConfigurations;
    private Map<String, String> config;

    @Before
    public void setUp() throws Exception {
        dbConfigurations = new DbConfigurations();
        config = new HashMap<>();
        config.put(DbConfig.NAME, "name1");
        config.put(DbConfig.DRIVER, "driver1");

    }

    @Test
    public void provides_config_if_servicePid_exists() throws Exception {
        config.put(Constants.SERVICE_PID, "pid");
        dbConfigurations.activate(config);
        DbConfig dbConfig = dbConfigurations.getConfig();
        assertThat(dbConfig.getDriver(), is(equalTo("driver1")));
        assertThat(dbConfig.get("driver"), is(equalTo("driver1")));
    }

    @Test
    public void doesnt_provide_config_if_servicePid_does_not_exist() throws Exception {
        dbConfigurations.activate(config);
        DbConfig dbConfig = dbConfigurations.getConfig();
        assertThat(dbConfig, is(nullValue()));
    }

}

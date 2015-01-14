package io.skysail.server.db.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.DbConfig;
import io.skysail.server.db.DbConfigurations;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DbConfigurationsTest {

    private DbConfigurations dbConfigurations;
    private Map<String, String> config1;
    private Map<String, String> config2;

    @Before
    public void setUp() throws Exception {
        dbConfigurations = new DbConfigurations();
        config1 = new HashMap<>();
        config1.put(DbConfig.NAME, "name1");
        config1.put(DbConfig.DRIVER, "driver1");

        config2 = new HashMap<>();
        config2.put(DbConfig.NAME, "name2");
        config2.put(DbConfig.DRIVER, "driver2");

    }

    @Test
    public void provides_configs_by_name() throws Exception {
        dbConfigurations.activate(config1);
        dbConfigurations.activate(config2);
        DbConfig dbConfig = dbConfigurations.getConfig("name1");
        assertThat(dbConfig.getDriver(), is(equalTo("driver1")));
        assertThat(dbConfig.get("driver"), is(equalTo("driver1")));
        dbConfig = dbConfigurations.getConfig("name2");
        assertThat(dbConfig.getDriver(), is(equalTo("driver2")));
        assertThat(dbConfig.get("driver"), is(equalTo("driver2")));
    }

    @Test
    public void returns_null_for_unknown_configurationName() throws Exception {
        DbConfig dbConfig = dbConfigurations.getConfig("name1");
        assertThat(dbConfig, is(nullValue()));
    }

}

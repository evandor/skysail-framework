package io.skysail.server.db.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.DbConfig;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DbConfigTest {

    private Map<String, String> config;

    @Before
    public void setUp() throws Exception {
        config = new HashMap<String, String>();
        config.put(DbConfig.NAME, "thename");
        config.put(DbConfig.URL, "theurl");
        config.put(DbConfig.DRIVER, "thedriver");
        config.put(DbConfig.USERNAME, "theusername");
        config.put(DbConfig.PASSWORD, "thepassword");
    }

    @Test
    public void creates_proper_toString_representation() {
        DbConfig dbConfig = new DbConfig(config);
        assertThat(dbConfig.toString(),
                is(equalTo("DbConfig(name=thename, driver=thedriver, url=theurl, username=theusername)")));
    }

    @Test
    public void lets_user_access_defaultKeys_via_getter() throws Exception {
        DbConfig dbConfig = new DbConfig(config);
        assertThat(dbConfig.getDriver(), is(equalTo("thedriver")));
        assertThat(dbConfig.getUrl(), is(equalTo("theurl")));
        assertThat(dbConfig.getName(), is(equalTo("thename")));
        assertThat(dbConfig.getUsername(), is(equalTo("theusername")));
        assertThat(dbConfig.getPassword(), is(equalTo("thepassword")));
    }

    @Test
    public void lets_user_add_and_access_additional_keys_and_values() {
        config.put("additionalKey", "additionalValue");
        DbConfig dbConfig = new DbConfig(config);
        assertThat(dbConfig.get("additionalKey"), is(equalTo("additionalValue")));
    }

    @Test
    public void creates_defaultName_if_name_is_missing_in_config() throws Exception {
        config.remove(DbConfig.NAME);
        DbConfig dbConfig = new DbConfig(config);
        assertThat(dbConfig.getName(), is(equalTo("defaultname")));
    }
}

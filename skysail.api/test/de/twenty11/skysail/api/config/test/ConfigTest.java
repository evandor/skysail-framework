package de.twenty11.skysail.api.config.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.api.config.Config;

public class ConfigTest {

    private Config parentConfig;

    @Before
    public void setUp() throws Exception {
        parentConfig = new Config();
    }

    @Test
    public void configs_default_constructor_has_no_parent() {
        Config config = new Config();
        assertThat(config.getParent(), is(nullValue()));
    }

    @Test
    public void configs_to_string_gives_proper_info() {
        Config config = new Config(parentConfig, "key", "value");
        assertThat(config.toString(), containsString("key"));
        assertThat(config.toString(), containsString("value"));
    }

    @Test
    public void key_and_value_are_set() {
        Config config = new Config(parentConfig, "key", "value");
        assertThat(config.getKey(), is(equalTo("key")));
        assertThat(config.getValue(), is(equalTo("value")));
    }

    @Test
    public void configs_parent_knows_about_its_child() {
        Config config = new Config(parentConfig);
        assertThat(config.getParent(), is(equalTo(parentConfig)));
        assertThat(parentConfig.getChildren().size(), is(1));
    }

    @Test
    public void get_works() {
        Config config = new Config(parentConfig, "key", "value");
        new Config(config, "key1", "value1");
        assertThat(parentConfig.get("key").size(), is(1));
        assertThat(parentConfig.get("key").get(0).getKey(), is(equalTo("key1")));
        assertThat(parentConfig.get("key").get(0).getValue(), is(equalTo("value1")));
    }

    @Test
    public void getString_works() {
        Config config = new Config(parentConfig, "key", "value");
        new Config(config, "key1", "value1");
        assertThat(parentConfig.getString("key"), is(equalTo("value")));
    }

}

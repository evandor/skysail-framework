package io.skysail.server.features.strategy.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import io.skysail.server.features.strategy.UsernameActivationStrategy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.um.SkysailUser;
import de.twenty11.skysail.server.core.restlet.SecurityFeatures;

public class UsernameActivationStrategyTest {

    private SkysailUser user;
    private FeatureState state;
    private Map<String, String> config;
    private UsernameActivationStrategy strategy;

    @Before
    public void setUp() {
        user = new SkysailUser("username");
        config = new HashMap<String, String>();
        strategy = new UsernameActivationStrategy();
    }

    @Test
    public void provides_its_name() throws Exception {
        assertThat(strategy.getName(), is(equalTo("Users by name")));
    }

    @Test
    public void is_not_active_if_no_username_is_provided() {
        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE);
        assertThat(strategy.isActive(state, user), is(false));
    }

    @Test
    public void is_not_active_for_not_matching_username() {
        config.put("users", "ich, du, er");
        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE, config, true);
        assertThat(strategy.isActive(state, user), is(false));
    }

    @Test
    public void is_active_for_matching_username() {
        config.put("users", "admin, username");
        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE, config, true);
        assertThat(strategy.isActive(state, user), is(true));
    }

}

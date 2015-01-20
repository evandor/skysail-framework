package de.twenty11.skysail.api.features.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.FeatureState;

public class FeatureStateTest {

    private Feature featureToggle;
    private HashMap<String, String> config;

    @Before
    public void setUp() throws Exception {
        featureToggle = Mockito.mock(Feature.class);
        config = new HashMap<String, String>();
    }

    @Test
    public void test() {
        FeatureState featureState = new FeatureState(featureToggle);
        assertThat(featureState.getFeature(), is(equalTo(featureToggle)));
        assertThat(featureState.getConfig().size(), is(0));
        assertThat(featureState.getParameterNames().size(), is(0));
        assertThat(featureState.getParameters().size(), is(0));
    }

    @Test
    public void can_be_enabled_with_constructor() {
        FeatureState featureState = new FeatureState(featureToggle, config, true);
        assertThat(featureState.isEnabled(), is(true));
    }

    @Test
    public void can_be_disabled_with_constructor() {
        FeatureState featureState = new FeatureState(featureToggle, config, false);
        assertThat(featureState.isEnabled(), is(false));
    }

    @Test
    public void can_be_enabled() {
        FeatureState featureState = new FeatureState(featureToggle, config, false);
        featureState.setEnabled(true);
        assertThat(featureState.isEnabled(), is(true));
    }

    @Test
    public void can_be_disabled() {
        FeatureState featureState = new FeatureState(featureToggle, config, true);
        featureState.setEnabled(false);
        assertThat(featureState.isEnabled(), is(false));
    }
}

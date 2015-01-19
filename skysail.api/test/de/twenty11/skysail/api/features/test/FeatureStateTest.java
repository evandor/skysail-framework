package de.twenty11.skysail.api.features.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.FeatureState;

public class FeatureStateTest {

    private Feature featureToggle;

    @Before
    public void setUp() throws Exception {
        featureToggle = Mockito.mock(Feature.class);
    }

    @Test
    public void test() {
        FeatureState featureState = new FeatureState(featureToggle);
        assertThat(featureState.getFeature(), is(equalTo(featureToggle)));
        assertThat(featureState.getParameterMap().size(), is(0));
        assertThat(featureState.getParameterNames().size(), is(0));
        assertThat(featureState.getParameters().size(), is(0));
    }

    @Test
    public void can_be_enabled_with_constructor() {
        FeatureState featureState = new FeatureState(featureToggle, true);
        assertThat(featureState.isEnabled(), is(true));
    }

    @Test
    public void can_be_disabled_with_constructor() {
        FeatureState featureState = new FeatureState(featureToggle, false);
        assertThat(featureState.isEnabled(), is(false));
    }

    @Test
    public void can_be_enabled() {
        FeatureState featureState = new FeatureState(featureToggle, false);
        featureState.setEnabled(true);
        assertThat(featureState.isEnabled(), is(true));
    }

    @Test
    public void can_be_disabled() {
        FeatureState featureState = new FeatureState(featureToggle, true);
        featureState.setEnabled(false);
        assertThat(featureState.isEnabled(), is(false));
    }
}

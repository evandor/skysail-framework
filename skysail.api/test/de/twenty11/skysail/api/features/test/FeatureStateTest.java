package de.twenty11.skysail.api.features.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.features.FeatureToggle;

public class FeatureStateTest {

    private FeatureToggle featureToggle;

    @Before
    public void setUp() throws Exception {
        featureToggle = Mockito.mock(FeatureToggle.class);
    }

    @Test
    public void test() {
        FeatureState featureState = new FeatureState(featureToggle);
        assertThat(featureState.getFeature(), is(equalTo(featureToggle)));
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

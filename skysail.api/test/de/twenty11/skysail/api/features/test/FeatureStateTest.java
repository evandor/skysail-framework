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

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		FeatureToggle featureToggle = Mockito.mock(FeatureToggle.class);
		FeatureState featureState = new FeatureState(featureToggle);
		assertThat(featureState.getFeature(), is(equalTo(featureToggle)));
	}
}

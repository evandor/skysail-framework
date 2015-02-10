package io.skysail.server.features.repositories.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.features.Feature;
import io.skysail.api.features.FeatureState;
import io.skysail.server.features.repositories.FeaturesRepository;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeaturesRepositoryTest {

    private FeaturesRepository featuresRepository;
    private Map<String, String> configMap;
    private Feature feature;

    @Before
    public void setUp() {
        featuresRepository = new FeaturesRepository();
        configMap = new HashMap<>();
        feature = Mockito.mock(Feature.class);
    }

    @Test
    public void returns_enabled_state_if_feature_is_set_to_true() throws Exception {
        configMap.put("featureName", "true");
        featuresRepository.activate(configMap);
        Mockito.when(feature.name()).thenReturn("featureName");
        FeatureState featureState = featuresRepository.getFeatureState(feature);
        assertThat(featureState.isEnabled(), is(true));
    }

    @Test
    public void returns_disabled_state_if_feature_is_set_to_false() throws Exception {
        configMap.put("featureName", "false");
        featuresRepository.activate(configMap);
        Mockito.when(feature.name()).thenReturn("featureName");
        FeatureState featureState = featuresRepository.getFeatureState(feature);
        assertThat(featureState.isEnabled(), is(false));
    }

    @Test
    public void returns_disabled_state_if_feature_is_not_set() throws Exception {
        featuresRepository.activate(configMap);
        Mockito.when(feature.name()).thenReturn("featureName");
        FeatureState featureState = featuresRepository.getFeatureState(feature);
        assertThat(featureState.isEnabled(), is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setFeatureState_throws_Exception() throws Exception {
        featuresRepository.setFeatureState(null);
    }

    @Test
    public void deactivating_clears_configMap() throws Exception {
        configMap.put("featureName", "true");
        featuresRepository.activate(configMap);
        Mockito.when(feature.name()).thenReturn("featureName");
        featuresRepository.deactivate();
        FeatureState featureState = featuresRepository.getFeatureState(feature);
        assertThat(featureState.isEnabled(), is(false));

    }

}

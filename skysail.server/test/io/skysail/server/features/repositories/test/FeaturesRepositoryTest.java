package io.skysail.server.features.repositories.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.features.repositories.FeaturesRepository;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.FeatureState;

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

}

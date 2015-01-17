package de.twenty11.skysail.api.features.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.api.features.FeatureManager;
import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.features.FeatureToggle;
import de.twenty11.skysail.api.features.OsgiFeatureToggle;
import de.twenty11.skysail.api.features.StateRepository;

public class OsgiFeatureToggleTest {

    private OsgiFeatureToggle osgiFeatureToggle;

    @Before
    public void setUp() throws Exception {
        osgiFeatureToggle = new OsgiFeatureToggle();
    }

    @Test
    public void provides_its_name() {
        assertThat(osgiFeatureToggle.name(), is(equalTo(OsgiFeatureToggle.class.getName())));
    }

    @Test
    public void feature_is_not_active_by_default() {
        osgiFeatureToggle.setFeatureManager(null);
        assertThat(osgiFeatureToggle.isActive(), is(false));
    }

    @Test
    public void feature_is_not_active_if_featureManager_has_been_removed() {
        FeatureManager featureManager = Mockito.mock(FeatureManager.class);
        Mockito.when(featureManager.isActive(Mockito.<OsgiFeatureToggle> any())).thenReturn(true);
        osgiFeatureToggle.setFeatureManager(featureManager);
        osgiFeatureToggle.unsetFeatureManager(featureManager);
        assertThat(osgiFeatureToggle.isActive(), is(false));
    }

    @Test
    public void feature_is_not_active_if_featureManager_decides_so() {
        FeatureManager featureManager = Mockito.mock(FeatureManager.class);
        Mockito.when(featureManager.isActive(Mockito.<OsgiFeatureToggle> any())).thenReturn(false);
        osgiFeatureToggle.setFeatureManager(featureManager);
        assertThat(osgiFeatureToggle.isActive(), is(false));
    }

    @Test
    public void feature_is_active_if_featureManager_decides_so() {
        FeatureManager featureManager = Mockito.mock(FeatureManager.class);
        Mockito.when(featureManager.isActive(Mockito.<OsgiFeatureToggle> any())).thenReturn(true);
        osgiFeatureToggle.setFeatureManager(featureManager);
        assertThat(osgiFeatureToggle.isActive(), is(true));
    }

    @Test
    public void testName() {
        StateRepository stateRepository = osgiFeatureToggle.getStateRepository();
        FeatureToggle feature = Mockito.mock(FeatureToggle.class);
        FeatureState featureState = new FeatureState(feature);
        stateRepository.setFeatureState(featureState);
    }
}

package io.skysail.api.features.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FeatureTest {

    @Test
    public void features_are_not_active_by_default() throws Exception {
        assertThat(MyFeatures.FEATURE_ONE.isActive(), is(false));
    }

    @Test
    public void features_are_not_active_with_enabledByDefault_Annotation() throws Exception {
        assertThat(MyFeatures.FEATURE_TWO.isActive(), is(true));
    }

    @Test
    public void testName() throws Exception {
        assertThat(MyFeatures.FEATURE_ONE.name(), is("FEATURE_ONE"));
    }

}

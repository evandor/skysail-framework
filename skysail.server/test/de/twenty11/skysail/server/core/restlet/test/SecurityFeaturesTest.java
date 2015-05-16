package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.twenty11.skysail.server.core.restlet.SecurityFeatures;

public class SecurityFeaturesTest {

    @Test
    public void feature_is_not_active_by_default() throws Exception {
        assertThat(SecurityFeatures.ALLOW_ORIGIN_FEATURE.isActive(), is(false));
    }
}

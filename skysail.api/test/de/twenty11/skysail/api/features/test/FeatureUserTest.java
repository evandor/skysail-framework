package de.twenty11.skysail.api.features.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.twenty11.skysail.api.features.FeatureUser;

public class FeatureUserTest {

    @Test
    public void testName() {
        FeatureUser user = new FeatureUser("name");
        assertThat(user.getName(), is(equalTo("name")));
    }

}

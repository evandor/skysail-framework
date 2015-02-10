package io.skysail.api.features.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.features.FeatureUser;

import org.junit.Test;

public class FeatureUserTest {

    @Test
    public void testName() {
        FeatureUser user = new FeatureUser("name");
        assertThat(user.getName(), is(equalTo("name")));
    }

}

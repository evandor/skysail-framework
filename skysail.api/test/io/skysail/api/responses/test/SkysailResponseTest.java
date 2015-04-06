package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import io.skysail.api.responses.SkysailResponse;

import org.junit.Before;
import org.junit.Test;

public class SkysailResponseTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testName() {
        SkysailResponse<String> response = new SkysailResponse<String>("entity");
        assertThat(response.getEntity(),is(equalTo("entity")));
        assertThat(response.isForm(),is(false));
    }
}

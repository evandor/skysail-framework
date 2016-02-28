package io.skysail.server.app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.app.ApiVersion;

public class ApiVersionTest {

    private ApiVersion apiVersion;

    @Before
    public void setUp() {
        apiVersion = new ApiVersion(22);
    }

    @Test(expected = NullPointerException.class)
    public void nullValue_yields_exception() {
        apiVersion = new ApiVersion(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroValue_yields_exception() {
        apiVersion = new ApiVersion(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeValue_yields_exception() {
        apiVersion = new ApiVersion(-11);
    }

    @Test
    public void getVersionPath() {
        assertThat(apiVersion.getVersionPath(),is("/v22"));
    }

    @Test
    public void testToString() {
        assertThat(apiVersion.toString(),is("v22"));
    }

    @Test
    public void equals() {
        assertThat(apiVersion.equals(new ApiVersion(22)),is(true));
        assertThat(apiVersion.equals(new ApiVersion(33)),is(false));
    }

}

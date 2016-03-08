package io.skysail.server.utils.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;

import io.skysail.server.utils.BundleUtils;

@RunWith(MockitoJUnitRunner.class)
public class BundleUtilsTest {
    
    @Mock
    private Logger log;
    
    @Mock
    private Bundle bundle;

    @Before
    public void setUp() throws Exception {
        URL dummyFileUrl = new File("./etc/.gitignore").toURI().toURL();
        when(bundle.getResource("path")).thenReturn(dummyFileUrl);
    }

    @Test
    public void readResource() {
        String content = BundleUtils.readResource(bundle, "path");
        assertThat(content.length(),greaterThan(10));
    }

    @Test
    public void getEntryPaths_delegates_to_bundle() {
        BundleUtils.getEntryPaths(bundle, "path");
        verify(bundle).getEntryPaths("path");
    }

}

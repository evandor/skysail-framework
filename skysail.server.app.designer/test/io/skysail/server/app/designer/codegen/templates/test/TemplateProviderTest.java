package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;

import io.skysail.server.app.designer.codegen.templates.TemplateProvider;

public class TemplateProviderTest {

    private String currentDir;

    @Before
    public void setUp() throws Exception {
        currentDir = new File(".").getAbsolutePath();
    }

    @Test
    public void testName() throws MalformedURLException {
        Bundle bundle = Mockito.mock(Bundle.class);
        URL url = new URL("file://"+currentDir+"/resources/code");
        Mockito.when(bundle.getResource("/code")).thenReturn(url);
        TemplateProvider templateProvider = new TemplateProvider(bundle);
        assertThat(templateProvider.templateFor("application.stg"),is(notNullValue()));
    }
}

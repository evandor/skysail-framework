package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.*;

import org.junit.*;
import org.mockito.Mockito;
import org.osgi.framework.*;
import org.osgi.service.component.ComponentContext;

import io.skysail.server.app.designer.codegen.templates.TemplateProvider;

public class TemplateProviderTest {

    private String currentDir;

    @Before
    public void setUp() throws Exception {
        currentDir = new File(".").getAbsolutePath();
    }

    @Test
    @Ignore // works on unix only?
    public void testName() throws MalformedURLException {
        Bundle bundle = Mockito.mock(Bundle.class);
        URL url = new URL("file://"+currentDir+"/resources/code");
        Mockito.when(bundle.getResource("/code")).thenReturn(url);
        TemplateProvider templateProvider = new TemplateProvider();
        ComponentContext componentContext = Mockito.mock(ComponentContext.class);
        BundleContext bundleContext = Mockito.mock(BundleContext.class);
        Mockito.when(componentContext.getBundleContext()).thenReturn(bundleContext);
        Mockito.when(bundleContext.getBundle()).thenReturn(bundle);
        templateProvider.activate(componentContext);

        assertThat(templateProvider.templateFor("application.stg"),is(notNullValue()));
    }
}

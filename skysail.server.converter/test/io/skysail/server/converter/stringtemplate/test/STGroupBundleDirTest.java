package io.skysail.server.converter.stringtemplate.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.converter.stringtemplate.STGroupBundleDir;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.ST;

public class STGroupBundleDirTest {

    private STGroupBundleDir groupBundleDir;

    @Before
    public void setUp() throws Exception {
        Resource resource = Mockito.mock(Resource.class);
        // Mockito.doReturn(EntityServerResource.class).when(resource.getClass());
        Bundle bundle = Mockito.mock(Bundle.class);
        Mockito.when(bundle.getSymbolicName()).thenReturn("symbolicName");
        Mockito.when(bundle.getVersion()).thenReturn(new Version("1.0.0"));
        Mockito.when(bundle.getResource("resourcePath")).thenReturn(new URL("file://localhost"));
        groupBundleDir = new STGroupBundleDir(bundle, resource, "resourcePath");
    }

    @Test
    public void testSTGroupBundleDir() throws Exception {
        assertThat(groupBundleDir.toString(), containsString("symbolicName"));
        assertThat(groupBundleDir.toString(), containsString("1.0.0"));
    }

    @Test
    public void unknown_InstanceOf_yields_null_value() throws Exception {
        ST instanceOf = groupBundleDir.getInstanceOf("unknown");
        assertThat(instanceOf, is(nullValue()));
    }

}

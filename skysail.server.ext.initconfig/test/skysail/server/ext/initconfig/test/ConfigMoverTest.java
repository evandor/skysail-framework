package skysail.server.ext.initconfig.test;

import java.net.URL;
import java.util.*;

import org.junit.*;
import org.mockito.Mockito;
import org.osgi.framework.*;
import org.osgi.service.component.ComponentContext;

import skysail.server.ext.initconfig.ConfigMover;

public class ConfigMoverTest {

    private ConfigMover configMover;
    private ComponentContext context;

    @Before
    public void setUp() throws Exception {
        configMover = new ConfigMover();
        context = Mockito.mock(ComponentContext.class);
        BundleContext bundleContext = Mockito.mock(BundleContext.class);
        Mockito.when(context.getBundleContext()).thenReturn(bundleContext);
        Bundle theBundle = Mockito.mock(Bundle.class);
        Mockito.when(bundleContext.getBundles()).thenReturn(new Bundle[] {theBundle});
        Mockito.when(theBundle.getSymbolicName()).thenReturn("product.bundle");
        Mockito.when(theBundle.getResource("thepath")).thenReturn(new URL("file://theResourceUrl"));
        Enumeration<String> entryPaths = new Vector<String>(Arrays.asList("thepath")).elements();
        Mockito.when(theBundle.getEntryPaths("configpath")).thenReturn(entryPaths);
    }

    @Test
    public void does_nothing_if_product_bundle_is_not_found() throws Exception {
        configMover.activate(context);
    }

    @Test
    public void does_nothing_if_no_config_is_found() {
        System.setProperty(ConfigMover.PRODUCT_BUNDLE_IDENTIFIER, "product.bundle");
        configMover.activate(context);
    }

    @Test
    @Ignore // to be done
    public void testMe() {
        System.setProperty(ConfigMover.PRODUCT_BUNDLE_IDENTIFIER, "product.bundle");
        System.setProperty(ConfigMover.CONFIG_SOURCE_SYSTEM_PROPERTY_IDENTIFIER, "configpath");
        configMover.activate(context);
    }
}

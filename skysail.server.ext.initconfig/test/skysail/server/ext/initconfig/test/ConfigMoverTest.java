//package skysail.server.ext.initconfig.test;
//
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.util.Enumeration;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.Vector;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.osgi.service.component.ComponentContext;
//
//import skysail.server.ext.initconfig.ConfigMover;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ConfigMoverTest {
//
//    @Spy
//    private ConfigMover configMover;
//    
//    @Mock
//    private ComponentContext context;
//
//    @Mock
//    private Bundle theBundle;
//    
//    @Mock
//    private BundleContext bundleContext;
//    
//    @Before
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    public void setUp() throws Exception {
//        Set<String> set = new HashSet<>();
//        set.add("config/targetPath");
//        Enumeration<String> entryPaths = new Vector(set).elements();
//
//        when(context.getBundleContext()).thenReturn(bundleContext);
//        when(bundleContext.getBundles()).thenReturn(new Bundle[] { theBundle });
//        when(theBundle.getSymbolicName()).thenReturn("product.bundle");
//        when(theBundle.getResource("config/targetPath")).thenReturn(new File("./config/test.cfg").toURI().toURL());
//        when(theBundle.getEntryPaths("default")).thenReturn(entryPaths);
//    }
//
//    @Test
//    public void does_nothing_if_product_bundle_is_not_found() throws Exception {
//        configMover.activate(context);
//    }
//
//    @Test
//    public void does_nothing_if_no_config_is_found() {
//        System.setProperty(io.skysail.server.Constants.PRODUCT_BUNDLE_IDENTIFIER, "product.bundle");
//        configMover.activate(context);
//    }
//
//    @Test
//    public void testMe() {
//        System.setProperty(io.skysail.server.Constants.PRODUCT_BUNDLE_IDENTIFIER, "product.bundle");
//        //System.setProperty(ConfigMover.CONFIG_SOURCE_SYSTEM_PROPERTY_IDENTIFIER, "configpath");
//        configMover.activate(context);
//    }
//}

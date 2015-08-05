//package de.twenty11.skysail.server.osgi.osgimonitor.resources;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.wiring.BundleCapability;
//import org.osgi.framework.wiring.BundleRevision;
//import org.osgi.framework.wiring.BundleWire;
//import org.osgi.framework.wiring.BundleWiring;
//import org.restlet.resource.Get;
//
//import de.twenty11.skysail.server.core.restlet.ListServerResource;
//import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
//import de.twenty11.skysail.server.osgi.osgimonitor.domain.Capability;
//
////@Presentation(preferred = PresentationStyle.LIST2)
//public class CapabilitiesResource extends ListServerResource<Capability> {
//
//    public CapabilitiesResource() {
//        super(null);
//    }
//
//    @Override
//    @Get("html|json|csv")
//    public List<Capability> getEntities() {
//        return getEntities("All Capabilities");
//    }
//
//    @Override
//    public List<Capability> getData() {
//        List<Capability> result = new ArrayList<Capability>();
//        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
//
//        BundleContext bundleContext = app.getBundleContext();
//        Bundle[] bundles = bundleContext.getBundles();
//        for (Bundle bundle : bundles) {
//            addCapabilities(result, bundle);
//        }
//        return result;
//    }
//
//    private void addCapabilities(List<Capability> result, Bundle bundle) {
//        BundleWiring bw = bundle.adapt(BundleWiring.class);
//        if (bw == null) {
//            return;
//        }
//        List<BundleWire> providedWires = bw.getProvidedWires(BundleRevision.PACKAGE_NAMESPACE);
//
//        for (BundleCapability cap : bw.getCapabilities(BundleRevision.PACKAGE_NAMESPACE)) {
//            result.add(new Capability(cap, bundle));
//        }
//    }
//
//}

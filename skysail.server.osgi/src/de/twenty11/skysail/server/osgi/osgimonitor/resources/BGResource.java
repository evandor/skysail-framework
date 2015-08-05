//package de.twenty11.skysail.server.osgi.osgimonitor.resources;
//
//import org.osgi.framework.Bundle;
//
//import de.twenty11.skysail.api.structures.graph.Graph;
//import de.twenty11.skysail.api.structures.graph.Node;
//import de.twenty11.skysail.server.core.restlet.GraphResource;
//import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
//
//public class BGResource extends GraphResource {
//
//    private OsgiMonitorViewerApplication app;
//
//    public BGResource() {
//        app = (OsgiMonitorViewerApplication) getApplication();
//    }
//
//    @Override
//    public Graph getData() {
//        Graph g = new Graph("Bundles");
//        Bundle[] allBundles = app.getBundleContext().getBundles();
//        for (Bundle bundle : allBundles) {
//            if (bundle.getSymbolicName().contains("skysail")) {
//                g.add(new Node(bundle.getSymbolicName() + "_" + bundle.getVersion()));
//            }
//        }
////        for (Bundle bundle : allBundles) {
////            BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
////            List<BundleWire> requiredWires = bundleWiring.getRequiredWires("osgi.wiring.package");
////            for (BundleWire wire : requiredWires) {
////                Integer sourceIndex = g.getNodeIndex(bundle.getBundleId());
////                Integer targetIndex = g.getNodeIndex(wire.getProvider().getBundle().getBundleId());
////                if (sourceIndex != null && targetIndex != null && sourceIndex != targetIndex) {
////                    g.addEdge(sourceIndex, targetIndex);
////                }
////            }
////        }
//        return g;
//    }
//
//}

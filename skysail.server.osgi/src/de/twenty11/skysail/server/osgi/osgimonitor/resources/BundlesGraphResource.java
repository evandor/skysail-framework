//package de.twenty11.skysail.server.osgi.osgimonitor.resources;
//
//import java.util.List;
//
//import org.osgi.framework.Bundle;
//import org.osgi.framework.wiring.BundleWire;
//import org.osgi.framework.wiring.BundleWiring;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.EntityServerResource;
//import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
//import de.twenty11.skysail.server.osgi.osgimonitor.domain.NodesAndEdges;
//
//public class BundlesGraphResource extends EntityServerResource<NodesAndEdges> {
//
//	private OsgiMonitorViewerApplication app;
//
//	public BundlesGraphResource() {
//		app = (OsgiMonitorViewerApplication) getApplication();
//	}
//
//	@Override
//	public NodesAndEdges getData() {
//		NodesAndEdges nodesAndEdges = new NodesAndEdges();
//		Bundle[] allBundles = app.getBundleContext().getBundles();
//		for (Bundle bundle : allBundles) {
//			if (bundle.getSymbolicName().contains("skysail")) {
//				nodesAndEdges.addNode(bundle);
//			}
//		}
//		for (Bundle bundle : allBundles) {
//			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
//			List<BundleWire> requiredWires = bundleWiring.getRequiredWires("osgi.wiring.package");
//			for (BundleWire wire : requiredWires) {
//				Integer sourceIndex = nodesAndEdges.getNodeIndex(bundle.getBundleId());
//				Integer targetIndex = nodesAndEdges.getNodeIndex(wire.getProvider().getBundle().getBundleId());
//				if (sourceIndex != null && targetIndex != null && sourceIndex != targetIndex) {
//					nodesAndEdges.addEdge(sourceIndex, targetIndex);
//				}
//			}
//		}
//		return nodesAndEdges;
//	}
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public String getId() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//}

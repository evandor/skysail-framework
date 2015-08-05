//package de.twenty11.skysail.server.ext.osgimonitor.resources;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.InvalidSyntaxException;
//import org.osgi.framework.ServiceReference;
//import org.restlet.resource.Get;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.common.Presentation;
//import de.twenty11.skysail.common.PresentationStyle;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.common.responses.SuccessResponse;
//import de.twenty11.skysail.server.core.restlet.SkysailServerResource2;
//import de.twenty11.skysail.server.ext.osgimonitor.OsgiMonitorViewerApplication;
//
///**
// * 
// */
//@Presentation(preferred = PresentationStyle.D3_SIMPLE_GRAPH)
//public class BundlesAsD3GraphResource extends SkysailServerResource2 { // extends ListServerResource<NodeProvider> {
//
//    private List<Bundle> bundles;
//
//    /** deals with json objects */
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public BundlesAsD3GraphResource() {
//        setName("osgimonitor 'bundles as Javascript Graph' resource");
//        setDescription("The resource containing the list of bundles displayed as a graph");
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
//        BundleContext bundleContext = app.getBundleContext();
//        if (bundleContext == null) {
//            bundles = Collections.emptyList();
//        } else {
//            bundles = Arrays.asList(bundleContext.getBundles());
//        }
//    }
//
//    @Get("html")
//    public SkysailResponse<List<Map<String, String>>> getJSGraph() throws JsonParseException, JsonMappingException,
//            IOException {
//
//        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
//        BundleContext bundleContext = app.getBundleContext();
//        try {
//            ServiceReference[] allServiceReferences = bundleContext.getAllServiceReferences(null, null);
//            for (ServiceReference sr : allServiceReferences) {
//                Map<String, String> row = new HashMap<String, String>();
//                String source = sr.getBundle().getSymbolicName();
//                if (sr.getUsingBundles() != null) {
//                    for (Bundle usingBundle : sr.getUsingBundles()) {
//                        row.put("source", source);
//                        row.put("target", usingBundle.getSymbolicName());
//                        data.add(row);
//                    }
//                }
//            }
//        } catch (InvalidSyntaxException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        SuccessResponse<List<Map<String, String>>> response = new SkysailResponse<List<Map<String, String>>>(data);
//        return response;
//    }
//
// }

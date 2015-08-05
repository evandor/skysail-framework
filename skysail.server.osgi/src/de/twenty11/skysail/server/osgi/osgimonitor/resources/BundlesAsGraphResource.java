//package de.twenty11.skysail.server.osgi.osgimonitor.resources;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Dictionary;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;
//import org.restlet.resource.Get;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
//import de.twenty11.skysail.server.osgi.osgimonitor.domain.BundleDetails;
//import de.twenty11.skysail.server.osgi.osgimonitor.domain.ServiceReferenceDetails;
//import de.twenty11.skysail.server.restlet.ListServerResource;
//
///**
// * Restlet Resource class for handling Connections.
// * 
// * Provides a method to retrieve the existing connections and to add a new one.
// * 
// * The managed entity is of type {@link BundleDetails}, providing details (like jdbc url, username and password about
// * what is needed to actually connect to a datasource.
// * 
// */
//@Graph(nodesPath = "/osgimonitor/bundles", edgesPath = "/osgimonitor/services")
//public class BundlesAsGraphResource extends ListServerResource<NodeProvider> {
//
//    private List<Bundle> bundles;
//
//    public BundlesAsGraphResource() {
//        setName("osgimonitor bundles resource");
//        setDescription("The resource containing the list of bundles");
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
//    @Get("html|json")
//    public SkysailResponse<List<NodeProvider>> getBundles() {
//        List<NodeProvider> allBundlesAsNodes = new ArrayList<NodeProvider>();
//        List<BundleDetails> allBundles = allBundles();
//        for (BundleDetails bundleDetails : allBundles) {
//            allBundlesAsNodes.add(bundleDetails.asNode());
//        }
//        return getEntities(allBundlesAsNodes, "all Bundles as Graph");
//    }
//
//    // protected Response<List<NodeProvider>> getEntities2(List<NodeProvider> data, String defaultMsg) {
//    // try {
//    // RestletOsgiApplication app = (RestletOsgiApplication) getApplication();
//    // Set<String> mappings = app.getUrlMappingServiceListener() != null ? app.getUrlMappingServiceListener()
//    // .getMappings() : null;
//    // Reference ref = getReference();
//    //
//    // for (NodeProvider payload : data) {
//    // if (payload instanceof DetailsLinkProvider) {
//    // Map<String, String> links = new HashMap<String, String>();
//    // DetailsLinkProvider dlp = (DetailsLinkProvider) payload;
//    // for (Entry<String, String> linkEntry : dlp.getLinkMap().entrySet()) {
//    // links.put(linkEntry.getKey(), ref.toString() + linkEntry.getValue() + "?media=json");
//    // }
//    // dlp.setLinks(links);
//    // }
//    //
//    // }
//    //
//    // SuccessResponse<List<NodeProvider>> successResponse = new SkysailResponse<List<NodeProvider>>(data,
//    // getRequest(),
//    // mappings);
//    // successResponse.setMessage(defaultMsg);
//    // if (this.getMessage() != null && !"".equals(this.getMessage().trim())) {
//    // successResponse.setMessage(getMessage());
//    // }
//    // return successResponse;
//    // } catch (Exception e) {
//    // // log4jLogger.error(e.getMessage(), e);
//    // return new FailureResponse<List<NodeProvider>>(e);
//    // }
//    // }
//    //
//    // @Post
//    // public Representation install(String location) {
//    // String prefix = "prefix";
//    // if (!location.startsWith(prefix)) {
//    // return new StringRepresentation("location didn't start with '" + prefix + "'");
//    // }
//    // OsgiMonitorViewerApplication application = (OsgiMonitorViewerApplication) getApplication();
//    // // application
//    // return new StringRepresentation("success");
//    // }
//
//    private List<BundleDetails> allBundles() {
//        List<BundleDetails> result = new ArrayList<BundleDetails>();
//        // List<Bundle> bundles = Activator.getBundles();
//        for (Bundle bundle : bundles) {
//            BundleDetails bundleDetail = new BundleDetails(null);
//            bundleDetail.setSymbolicName(bundle.getLocation());
//            bundleDetail.setBundleId(bundle.getBundleId());
//            // bundleDetail.setHeaders(getDetails(bundle.getHeaders()));
//            bundleDetail.setLastModified(bundle.getLastModified());
//            bundleDetail.setRegisteredServices(getDetails(bundle.getRegisteredServices()));
//            bundleDetail.setServicesInUse(getDetails(bundle.getServicesInUse()));
//            bundleDetail.setState(bundle.getState());
//            bundleDetail.setVersion(bundle.getVersion());
//            bundleDetail.setSymbolicName(bundle.getSymbolicName());
//            result.add(bundleDetail);
//        }
//        return result;
//    }
//
//    private Map<String, String> getDetails(Dictionary headers) {
//        Map<String, String> result = new HashMap<String, String>();
//        Enumeration keys = headers.keys();
//        while (keys.hasMoreElements()) {
//            Object nextElement = keys.nextElement();
//            result.put(nextElement.toString(), headers.get(nextElement).toString());
//        }
//        return result;
//    }
//
//    private List<ServiceReferenceDetails> getDetails(ServiceReference[] registeredServices) {
//        List<ServiceReferenceDetails> details = new ArrayList<ServiceReferenceDetails>();
//        if (registeredServices == null) {
//            return details;
//        }
//        for (ServiceReference serviceReference : registeredServices) {
//            ServiceReferenceDetails srd = new ServiceReferenceDetails();
//            srd.setBundleId(serviceReference.getBundle().getBundleId());
//            srd.setName(serviceReference.toString());
//            // srd.setPropertyKeys(serviceReference.getPropertyKeys());
//            srd.setUsingBundles(getDetails(serviceReference.getUsingBundles()));
//            details.add(srd);
//        }
//        return details;
//    }
//
//    private List<BundleDetails> getDetails(Bundle[] usingBundles) {
//        List<BundleDetails> details = new ArrayList<BundleDetails>();
//        if (usingBundles == null) {
//            return details;
//        }
//        for (Bundle bundle : usingBundles) {
//            BundleDetails bundleDetails = new BundleDetails(null);
//            bundleDetails.setBundleId(bundle.getBundleId());
//            bundleDetails.setSymbolicName(bundle.getSymbolicName());
//            bundleDetails.setVersion(bundle.getVersion());
//            details.add(bundleDetails);
//        }
//        return details;
//    }
//
// }

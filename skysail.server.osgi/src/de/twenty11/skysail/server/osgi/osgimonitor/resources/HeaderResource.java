//package de.twenty11.skysail.server.ext.osgimonitor.resources;
//
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.restlet.resource.Get;
//
//import de.twenty11.skysail.common.ext.osgimonitor.HeaderDescriptor;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.server.ext.osgimonitor.OsgiMonitorViewerApplication;
//import de.twenty11.skysail.server.restlet.UniqueResultServerResource;
//
///**
// * Created with IntelliJ IDEA.
// * User: graefca
// * Date: 04.04.13
// * Time: 17:19
// * To change this template use File | Settings | File Templates.
// */
//public class HeaderResource extends UniqueResultServerResource<HeaderDescriptor> {
//
//    private String bundleId;
//
//    public HeaderResource() {
//        setName("header resource");
//        setDescription("resource to show bundles header");
//    }
//
//    @Override
//    protected void doInit() {
//        bundleId = (String) getRequest().getAttributes().get("bundleId");
//        //Form Stringform = new Form(getRequest().getEntity());
//        //action = form.getFirstValue("action");
//    }
//
//    @Get("html|json")
//    public SkysailResponse<HeaderDescriptor> getHeader() {
//        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
//        BundleContext bundleContext = app.getBundleContext();
//        Bundle bundle = bundleContext.getBundle(Long.parseLong(bundleId));
//        HeaderDescriptor details = new HeaderDescriptor(bundle);
//        //registerCommand("start", new StartCommand(bundle));
//        //registerCommand("stop", new StopCommand(bundle));
//        //registerCommand("update", new UpdateCommand(bundle));
//        return getEntity(details);
//    }
//}

//package de.twenty11.skysail.server.mgt.load;
//
//import java.lang.management.ManagementFactory;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.EntityServerResource;
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class ServerLoadResource extends EntityServerResource<Double> {
//
//    public ServerLoadResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "Server Load");
//    }
//
//    @Override
//    public Double getData() {
//        return ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
//    }
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//        return null;
//    }
//
//    @Override
//    public String getId() {
//        return null;
//    }
//
//}

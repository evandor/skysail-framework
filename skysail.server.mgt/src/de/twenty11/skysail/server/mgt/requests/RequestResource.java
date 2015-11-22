//package de.twenty11.skysail.server.mgt.requests;
//
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.EntityServerResource;
//import de.twenty11.skysail.server.mgt.ManagementApplication;
//import de.twenty11.skysail.server.services.RequestResponseMonitor;
//
//public class RequestResource extends EntityServerResource<RequestDetails> {
//
//    private Long requestId;
//    private ManagementApplication app;
//
//    public RequestResource() {
//        app = (ManagementApplication) getApplication();
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        requestId = Long.valueOf((String) getRequest().getAttributes().get("id"));
//    }
//
//    @Override
//    public RequestDetails getData() {
//        RequestResponseMonitor requestResponseMonitor = app.getRequestResponseMonitor();
//        if (requestResponseMonitor == null) {
//            return null;
//        }
//        return new RequestDetails(requestResponseMonitor.getRequest(requestId));
//    }
//
//    public SkysailResponse<?> addEntity(RequestDetails entity) {
//        return null;
//    }
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

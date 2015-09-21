//package de.twenty11.skysail.server.resources;
//
//import io.skysail.api.domain.Identifiable;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.restlet.resources.EntityServerResource;
//import de.twenty11.skysail.server.app.SkysailRootApplication;
//
//public class NameResource extends EntityServerResource<Identifiable> {
//
//    private SkysailRootApplication app;
//
//    public NameResource() {
//        app = (SkysailRootApplication) getApplication();
//    }
//
//    @Override
//    public Ide getEntity() {
//        return "productName"; //app.getConfigForKey("productName");
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

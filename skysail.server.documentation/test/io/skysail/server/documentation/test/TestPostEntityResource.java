//package io.skysail.server.documentation.test;
//
//import io.skysail.api.domain.Identifiable;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.restlet.resources.PostEntityServerResource;
//
//public class TestPostEntityResource extends PostEntityServerResource<Identifiable> {
//
//    public TestPostEntityResource() {
//        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
//    }
//
//    @Override
//    public Identifiable createEntityTemplate() {
//        return "template";
//    }
//
//    @Override
//    public SkysailResponse<String> addEntity(Identifiable entity) {
//        return new SkysailResponse<String>("added");
//    }
//
//}

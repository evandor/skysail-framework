package io.skysail.server.app.wiki.application;
//package io.skysail.server.app.wiki.application;
//
//import io.skysail.server.app.wiki.wikiApplication;
//
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class PostEntityResource extends PostEntityServerResource<Application> {
//
//    private wikiApplication app;
//
//    public PostEntityResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "Create new Application");
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        app = (wikiApplication) getApplication();
//    }
//
//    @Override
//    public Application createEntityTemplate() {
//        return new Application();
//    }
//
//    @Override
//    public SkysailResponse<?> addEntity(Application entity) {
//        app.getRepository().add(entity);
//        return new SkysailResponse<String>();
//    }
//
// }

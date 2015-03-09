package io.skysail.server.app.wiki.application;
//package io.skysail.server.app.wiki.application;
//
//import io.skysail.server.app.wiki.wikiApplication;
//
//import java.util.List;
//
//import de.twenty11.skysail.api.responses.Linkheader;
//import de.twenty11.skysail.server.core.restlet.ListServerResource;
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class EntitiesResource extends ListServerResource<Application> {
//
//    private wikiApplication app;
//
//    public EntitiesResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
//    }
//
//    @Override
//    protected void doInit() {
//        app = (wikiApplication) getApplication();
//    }
//
//    @Override
//    public List<Application> getEntity() {
//        return app.getRepository().findAll(Application.class);
//    }
//
//    @Override
//    public List<Linkheader> getLinkheader() {
//        return super.getLinkheader(PostEntityResource.class);
//    }
//
// }

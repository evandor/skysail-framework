//package io.skysail.server.app.designer.entities.resources;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.app.designer.DesignerApplication;
//import io.skysail.server.app.designer.application.DbApplication;
//import io.skysail.server.app.designer.entities.DbEntity;
//import io.skysail.server.restlet.resources.ListServerResource;
//
//import java.util.*;
//import java.util.function.Consumer;
//
//import org.restlet.resource.ResourceException;
//
//public class SubEntitiesResource extends ListServerResource<DbEntity> {
//
//    private DesignerApplication app;
//    private String id;
//    private String entityId;
//
//    public SubEntitiesResource() {
//        super(SubEntityResource.class);
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        super.doInit();
//        app = (DesignerApplication) getApplication();
//        id = getAttribute("id");
//        entityId = getAttribute(DesignerApplication.ENTITY_ID);
//    }
//
//    @Override
//    public List<DbEntity> getEntity() {
//        DbApplication application = app.getRepository().getById(DbApplication.class, id);
//        Optional<DbEntity> parentEntity = null;// app.getEntityFromApplication(application, entityId);
//        if(parentEntity.isPresent()) {
//            return parentEntity.get().getSubEntities();
//        }
//        return Collections.emptyList();
//    }
//
//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostEntityResource.class);
//    }
//
//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> l.substitute("id", id).substitute(DesignerApplication.ENTITY_ID, entityId);
//    }
//
//}

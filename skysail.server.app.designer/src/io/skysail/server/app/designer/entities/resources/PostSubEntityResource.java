//package io.skysail.server.app.designer.entities.resources;
//
//import java.util.function.Consumer;
//
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//import io.skysail.api.links.Link;
//import io.skysail.server.app.designer.DesignerApplication;
//import io.skysail.server.app.designer.application.DbApplication;
//import io.skysail.server.app.designer.entities.DbEntity;
//import io.skysail.server.app.designer.repo.DesignerRepository;
//import io.skysail.server.restlet.resources.PostEntityServerResource;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class PostSubEntityResource extends PostEntityServerResource<DbEntity> {
//
//    private DesignerApplication app;
//    private String id;
//    private String entityId;
//    private DesignerRepository repo;
//
//    public PostSubEntityResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "Create 1:n Relation");
//        removeFromContext(ResourceContextId.LINK_GLYPH);
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        super.doInit();
//        app = (DesignerApplication) getApplication();
//        id = getAttribute("id");
//        entityId = getAttribute(DesignerApplication.ENTITY_ID);
//        repo = (DesignerRepository) app.getRepository(DbApplication.class);
//    }
//
//    @Override
//    public DbEntity createEntityTemplate() {
//        return new DbEntity();
//    }
//
//    @Override
//    public void addEntity(DbEntity entity) {
//        DbEntity parentEntity = repo.getById(DbEntity.class, entityId);
//        parentEntity.getSubEntities().add(entity);
//        repo.update(parentEntity, "subEntities");
//    }
//
//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> l.substitute("id", id).substitute(DesignerApplication.ENTITY_ID, entityId);
//    }
//}

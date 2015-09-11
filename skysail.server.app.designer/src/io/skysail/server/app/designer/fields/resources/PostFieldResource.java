package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import com.orientechnologies.orient.core.id.ORecordId;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostFieldResource extends PostEntityServerResource<EntityField> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    public PostFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new EntityField");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
    }

    @Override
    public EntityField createEntityTemplate() {
        return new EntityField();
    }

    @Override
    public SkysailResponse<EntityField> addEntity(EntityField field) {

        ORecordId added = (ORecordId) DesignerRepository.add(field);

        Entity entity= app.getRepository().getById(Entity.class, id);
        entity.getFields().add(added.getIdentity().toString());
        app.getRepository().update(entity, "fields");
        return new SkysailResponse<>();


//        Entity entity = app.getRepository().getById(Entity.class, entityId);
//        entity.getFields().add(field);
//        app.getRepository().update(entity);
//        return new SkysailResponse<String>();
    }


//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> l.substitute("id", id).substitute(DesignerApplication.ENTITY_ID, entityId);
//    }

}

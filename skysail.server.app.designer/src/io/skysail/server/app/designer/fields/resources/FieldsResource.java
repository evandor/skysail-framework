package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.function.Consumer;

public class FieldsResource extends ListServerResource<EntityField> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    public FieldsResource() {
        super(FieldResource.class);
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);

//        Application application = app.getRepository().getById(Application.class, id);
//        Map<String, String> substitutions = new HashMap<>();
//        substitutions.put("/applications/" + id, application.getName());
//        Entity entity = app.getRepository().getById(Entity.class, entityId);
//        substitutions.put("/entities/" + entityId, entity.getName());
//        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
    }

    @Override
    public List<EntityField> getEntity() {
        //Application application = app.getApplication(id);
        Entity entity = app.getEntity(entityId);
        if (entity != null) {
            return Collections.emptyList();//entity.getFields();
        }
        return null;//entity.getFields()
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostFieldResource.class, PostActionFieldResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            if (id != null) {
                l.substitute("id", id).substitute(DesignerApplication.ENTITY_ID, entityId);
            }
        };
    }
}

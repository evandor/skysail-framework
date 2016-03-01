package io.skysail.server.app.designer.relations.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.relations.DbRelation;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.ListServerResource;

public class RelationsResource extends ListServerResource<DbRelation> {

    private DesignerApplication app;

    public RelationsResource() {
        super(RelationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "relations");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication)getApplication();
    }
    
    @Override
    public List<?> getEntity() {
         List<DbEntity> oneToManyRelations = app.getRepository().findEntity(getAttribute("eid")).getOneToManyRelations();
         return oneToManyRelations.stream().map(this::createRelation).collect(Collectors.toList());
    }

    private DbRelation createRelation(DbEntity rel) { // NOSONAR
        DbRelation result = new DbRelation();
        result.setId(rel.getId());
        result.setName(rel.getName());
        result.setRelationType("One to many");
        result.setTarget(rel.getName());
        return result;
    }

    @Override
    public List<TreeStructure> getTreeRepresentation() {
        return app.getTreeRepresentation(getAttribute("id"));
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostRelationResource.class);
    }


}

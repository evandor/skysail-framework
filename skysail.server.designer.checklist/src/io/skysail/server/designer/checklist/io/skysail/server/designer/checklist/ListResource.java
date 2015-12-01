package io.skysail.server.designer.checklist;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class Resource extends EntityServerResource<pkg.> {

    private String id;
    private ChecklistApplication app;

    public Resource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (ChecklistApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        return new SkysailResponse<String>();
    }

    @Override
    public  getEntity() {
        return app.getRepository().getById(.class, id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutListResource.class);
    }

	/*
	Application:
	CodegenApplicationModel(applicationName=Checklist, entityModels=[EntityModel(id=io.skysail.server.designer.checklist.List, fields={listname=CodegenFieldModel(name=listname)}, postResource=Resource(links=null), putResource=Resource(links=null), listResource=Resource(links=null), entityResource=Resource(links=null))], packageName=io.skysail.server.designer.checklist, path=../, projectName=skysail.server.designer.checklist)

	Entity:
	EntityModel(id=io.skysail.server.designer.checklist.List, fields={listname=CodegenFieldModel(name=listname)}, postResource=Resource(links=null), putResource=Resource(links=null), listResource=Resource(links=null), entityResource=Resource(links=null))
	entity.fields:
	listname
	*/
}
package io.skysail.app.propman;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class CampaignResource extends EntityServerResource<Campaign> {

    private String id;
    private PropManApplication app;

    public CampaignResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PropManApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        return new SkysailResponse<String>();
    }

    @Override
    public Campaign getEntity() {
        return app.getRepository().getById(Campaign.class, id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutCampaignResource.class,PostRequestResource.class);
    }

	/*
	Application:
	ApplicationModel(applicationName=PropMan, entityModels=[EntityModel(entityName=Campaign), EntityModel(entityName=Request)], packageName=io.skysail.app.propman, path=../, projectName=skysail.server.app.designer.propman)

	Entity:
	EntityModel(entityName=Campaign)
	entity.fields:
	FieldModel(name=name)
	*/
}
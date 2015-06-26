package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostCampaignResource extends PostEntityServerResource<Campaign> {

	private PropManApplication app;

    public PostCampaignResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Campaign");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PropManApplication) getApplication();
    }

    @Override
    public Campaign createEntityTemplate() {
        return new Campaign();
    }

    @Override
    public SkysailResponse<?> addEntity(Campaign entity) {
         Subject subject = SecurityUtils.getSubject();
        //entity.setOwner(subject.getPrincipal().toString());
        String id = app.getRepository().add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CampaignsResource.class);
    }
}
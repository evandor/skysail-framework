package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostRequestResource extends PostEntityServerResource<Request> {

	private PropManApplication app;
    private String campaignId;

    public PostRequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Request");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PropManApplication) getApplication();
        campaignId = getAttribute("id");
    }

	@Override
    public Request createEntityTemplate() {
        return new Request();
    }

    @Override
    public SkysailResponse<?> addEntity(Request entity) {
        
        Campaign campaign = ((PropManApplication) getApplication()).getRepository().getById(Campaign.class, campaignId);
        campaign.addRequest(entity);
        ((PropManApplication) getApplication()).getRepository().update(campaignId, campaign);
        
//        Subject subject = SecurityUtils.getSubject();
//        //entity.setOwner(subject.getPrincipal().toString());
//        String id = ((PropManApplication) getApplication()).getRepository().add(entity).toString();
//        entity.setId(id);
        return new SkysailResponse<String>();
    }
    
    //@Override
    //public String redirectTo() {
    //    return super.redirectTo(ApplicationsResource.class);
    //}
   
}

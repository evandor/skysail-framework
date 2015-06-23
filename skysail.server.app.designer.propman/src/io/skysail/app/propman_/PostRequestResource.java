package io.skysail.app.propman_;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostRequestResource extends PostEntityServerResource<Request> {

	private PropManApplication app;
	private String id;
	

    public PostRequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Request");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PropManApplication) getApplication();
        id = getAttribute("id");
        
    }

	@Override
    public Request createEntityTemplate() {
        return new Request();
    }

    @Override
    public SkysailResponse<?> addEntity(Request entity) {
        Campaign parentEntity = app.getRepository().getById(Campaign.class, id);
        parentEntity.addRequest(entity);
        app.getRepository().update(id, parentEntity);
        return new SkysailResponse<String>();    }
    
   
}

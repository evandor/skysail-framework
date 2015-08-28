package de.twenty11.skysail.server.app.tutorial.model2rest.conclusion;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ConclusionResource extends EntityServerResource<String> {

	public ConclusionResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Conclusion");
	}

	@Override
	public String getEntity() {
		return "";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

    @Override
    public List<Link> getLinks() {
         return ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
    }


}

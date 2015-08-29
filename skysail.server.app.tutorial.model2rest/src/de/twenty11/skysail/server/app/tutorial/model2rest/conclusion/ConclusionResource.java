package de.twenty11.skysail.server.app.tutorial.model2rest.conclusion;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.app.tutorial.model2rest.*;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ConclusionResource extends EntityServerResource<Dummy> {

	public ConclusionResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Conclusion");
	}

	@Override
	public Dummy getEntity() {
		return new Dummy();
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

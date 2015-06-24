package io.skysail.app.propman;

import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class RequestsResource extends ListServerResource<Request> {

    public RequestsResource() {
        super(RequestResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Requests");
    }

    @Override
    public List<Request> getEntity() {
        return ((PropManApplication) getApplication()).getRepository().findAll("select from Request");
    }

    public List<Link> getLinks() {
       return super.getLinks(PostCampaignResource.class);
    }
}
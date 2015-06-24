package io.skysail.app.propman;

import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class CampaignsResource extends ListServerResource<Campaign> {

    public CampaignsResource() {
        super(CampaignResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Campaigns");
    }

    @Override
    public List<Campaign> getEntity() {
        return ((PropManApplication) getApplication()).getRepository().findAll("select from Campaign");
    }

    public List<Link> getLinks() {
       return super.getLinks(PostCampaignResource.class);
    }
}
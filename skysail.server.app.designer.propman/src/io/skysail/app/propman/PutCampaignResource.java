package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutCampaignResource extends PutEntityServerResource<Campaign> {


    private String id;
    private PropManApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PropManApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(Campaign  entity) {
        Campaign original = getEntity();
        copyProperties(original,entity);

        app.getRepository().update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public Campaign getEntity() {
        return app.getRepository().getById(Campaign.class, id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CampaignsResource.class);
    }
}
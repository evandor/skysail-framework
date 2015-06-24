package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

public class PutCampaignResource extends PutEntityServerResource<Campaign> {

    @Override
    public SkysailResponse<?> updateEntity(Campaign entity) {
        return null;
    }

    @Override
    public Campaign getEntity() {
        return null;
    }
}
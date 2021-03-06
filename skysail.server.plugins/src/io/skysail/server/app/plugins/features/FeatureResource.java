package io.skysail.server.app.plugins.features;

import io.skysail.api.links.*;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

public class FeatureResource extends EntityServerResource<Feature> {

    @Override
    public List<Link> getLinks() {
        // return super.getLinkheader(PostInstallationResource.class);
        return Arrays.asList(new Link.Builder("123/installations/").relation(LinkRelation.NEXT).title("install")
                .build());
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Feature getEntity() {
        return null;
    }

}

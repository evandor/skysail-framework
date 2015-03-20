package io.skysail.server.app.plugins.features;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.SkysailResponse;

import java.util.Arrays;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class FeatureResource extends EntityServerResource<Feature> {

    @Override
    public List<Link> getLinkheader() {
        // return super.getLinkheader(PostInstallationResource.class);
        return Arrays.asList(new Link.Builder("123/installations/").relation(LinkRelation.NEXT).title("install")
                .build());
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Feature getEntity() {
        return null;
    }

}

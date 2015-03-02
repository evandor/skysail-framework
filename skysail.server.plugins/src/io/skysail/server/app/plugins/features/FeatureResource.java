package io.skysail.server.app.plugins.features;

import java.util.Arrays;
import java.util.List;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class FeatureResource extends EntityServerResource<Feature> {

    @Override
    public List<Linkheader> getLinkheader() {
        // return super.getLinkheader(PostInstallationResource.class);
        return Arrays.asList(new Linkheader.Builder("123/installations/").relation(LinkHeaderRelation.NEXT)
                .title("install").build());
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

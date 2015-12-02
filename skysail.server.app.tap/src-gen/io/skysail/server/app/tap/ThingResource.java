package io.skysail.server.app.tap;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ThingResource extends EntityServerResource<Thing> {

    private TapApplication app;
    private ThingRepo repository;

    protected void doInit() {
        super.doInit();
        app = (TapApplication)getApplication();
        repository = (ThingRepo) app.getRepository(Thing.class);
    }

    public Thing getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutThingResource.class);
    }

    public SkysailResponse<Thing> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}

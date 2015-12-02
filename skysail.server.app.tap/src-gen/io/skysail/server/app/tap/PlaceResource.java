package io.skysail.server.app.tap;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PlaceResource extends EntityServerResource<Place> {

    private TapApplication app;
    private PlaceRepo repository;

    protected void doInit() {
        super.doInit();
        app = (TapApplication)getApplication();
        repository = (PlaceRepo) app.getRepository(Place.class);
    }

    public Place getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutPlaceResource.class);
    }

    public SkysailResponse<Place> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}

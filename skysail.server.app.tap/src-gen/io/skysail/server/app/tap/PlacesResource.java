package io.skysail.server.app.tap;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PlacesResource extends ListServerResource<Place> {

    private TapApplication app;
    private PlaceRepo repository;

    public PlacesResource() {
        super(PlaceResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Places");
    }

    protected void doInit() {
        super.doInit();
        app = (TapApplication)getApplication();
        repository = (PlaceRepo) app.getRepository(Place.class);
    }

    @Override
    public List<Place> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}

package io.skysail.server.app.tap;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PlacesResource extends ListServerResource<Place> {

    private io.skysail.server.app.tap.TabApplication app;

    public PlacesResource() {
        super(PlaceResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Places");
    }

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.tap.TabApplication)getApplication();
    }

    @Override
    public List<Place> getEntity() {
        return app.getPlaceRepository().find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPlaceResource.class, PlacesResource.class,ThingsResource.class);
    }
}

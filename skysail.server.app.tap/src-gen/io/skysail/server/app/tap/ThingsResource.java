package io.skysail.server.app.tap;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ThingsResource extends ListServerResource<Thing> {

    private io.skysail.server.app.tap.TabApplication app;
    private ThingRepo repository;

    public ThingsResource() {
        super(ThingResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Things");
    }

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.tap.TabApplication)getApplication();
        repository = (ThingRepo) app.getRepository(Thing.class);
    }

    @Override
    public List<Thing> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostThingResource.class, PlacesResource.class, ThingsResource.class);
    }
}

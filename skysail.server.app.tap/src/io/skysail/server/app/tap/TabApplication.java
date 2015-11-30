package io.skysail.server.app.tap;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.app.*;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;

@Component(immediate = true)
public class TabApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public TabApplication() {
        super("TapApplication", new ApiVersion(1), Arrays.asList(Place.class, Thing.class));
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }

    @Override
    protected void attach() {
       super.attach();
       router.attach(new RouteBuilder("", ThingsResource.class));
    }

    public ThingRepo getThingRepository() {
        return (ThingRepo) getRepository(Thing.class);
    }

    public PlaceRepo getPlaceRepository() {
        return (PlaceRepo) getRepository(Place.class);
    }
}

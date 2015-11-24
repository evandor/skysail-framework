package io.skysail.server.app.tap;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.menus.MenuItemProvider;

import java.util.Arrays;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component(immediate = true)
public class TabApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public TabApplication() {
        super("TapApplication", new ApiVersion(1), Arrays.asList(Place.class, Thing.class));
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(DbRepository repo) {
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

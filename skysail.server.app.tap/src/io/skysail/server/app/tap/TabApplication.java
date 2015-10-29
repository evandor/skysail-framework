package io.skysail.server.app.tap;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;
import io.skysail.server.menus.*;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component(immediate = true)
public class TabApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "TapApplication";

    private static TabApplication instance;

    private DbRepository thingRepo,placeRepo;

    public TabApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Place.class, Thing.class));
        instance = this;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=ThingRepository)")
    public void setRepository(DbRepository repo) {
       this.thingRepo = (ThingRepo) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.thingRepo = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=PlaceRepository)")
    public void setPlaceRepository(DbRepository repo) {
       this.placeRepo = (PlaceRepo) repo;
    }

    public void unsetPlaceRepository(DbRepository repo) {
        this.placeRepo = null;
    }

    @Override
    protected void attach() {
       super.attach();
       router.attach(new RouteBuilder("", ThingsResource.class));
    }

    public ThingRepo getThingRepository() {
        return (ThingRepo) thingRepo;
    }

    public PlaceRepo getPlaceRepository() {
        return (PlaceRepo) placeRepo;
    }

    public static TabApplication getInstance() {
        return instance;
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

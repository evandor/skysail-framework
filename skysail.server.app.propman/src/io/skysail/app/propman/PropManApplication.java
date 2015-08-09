package io.skysail.app.propman;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.db.DbRepository;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
public class PropManApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "PropMan1";

    private PropManRepository repo;

    public PropManApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=PropManRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (PropManRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.repo = null;
    }

    public PropManRepository getRepository() {
        return repo;
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/Campaigns/", io.skysail.app.propman.PostCampaignResource.class));
        router.attach(new RouteBuilder("", io.skysail.app.propman.CampaignsResource.class));
        router.attach(new RouteBuilder("/Campaigns", io.skysail.app.propman.CampaignsResource.class));
        router.attach(new RouteBuilder("/Campaigns/{id}", io.skysail.app.propman.PutCampaignResource.class));

        router.attach(new RouteBuilder("/Campaigns/{id}/Requests/", PostRequestResource.class));



    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

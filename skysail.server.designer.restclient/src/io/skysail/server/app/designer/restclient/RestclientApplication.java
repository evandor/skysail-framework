package io.skysail.server.app.designer.restclient;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;
import io.skysail.server.menus.*;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@Component(immediate = true)
public class RestclientApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Restclient";

    public RestclientApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(ClientApplication.class));
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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
        router.attach(new RouteBuilder("/ClientApplication/{id}/execution", ExecutionResource.class));

    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }


}
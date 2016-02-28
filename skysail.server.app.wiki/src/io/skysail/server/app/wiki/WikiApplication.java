package io.skysail.server.app.wiki;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.menus.MenuItemProvider;

//@RequireUiKitWebResource(resource="css/bootstrap.css")
@Component(immediate = true)
public class WikiApplication extends WikiApplicationGen implements ApplicationProvider, MenuItemProvider {

    public  WikiApplication() {
        super("Wiki", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }

}
package io.skysail.server.app.notes;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.uikit.webresource.RequireUiKitWebResource;

@Component(immediate = true)
@RequireUiKitWebResource
public class NotesApplication extends NotesApplicationGen implements ApplicationProvider, MenuItemProvider {

    public  NotesApplication() {
        super("Notes", new ApiVersion(1), Arrays.asList());
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
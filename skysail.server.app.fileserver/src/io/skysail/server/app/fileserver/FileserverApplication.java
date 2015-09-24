package io.skysail.server.app.fileserver;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;

import java.util.*;
import java.util.stream.Collectors;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(configurationPolicy = ConfigurationPolicy.require)
public class FileserverApplication extends SkysailApplication  implements ApplicationProvider, MenuItemProvider {

    public static final String CONFIG_ROOT_PATH_IDENTIFIER = "fileserver.rootPath.";

    private static final String APP_NAME = "fileserver";

    private Map<String, String> config;

    public FileserverApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/folder_database.png");
    }

    @Activate
    public void activate(Map<String, String> config, ComponentContext componentContext) throws ConfigurationException {
        super.activate(componentContext);
        this.config = config;
    }

    @Deactivate
    public void deactivate() {
        this.config = null;
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", RootsResource.class));
        router.attach(new RouteBuilder("/", RootsResource.class));
        router.attach(new RouteBuilder("/{id}", ListFilesResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);

    }

    public List<RootPath> getRootPaths() {
        return config.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(CONFIG_ROOT_PATH_IDENTIFIER))
                .map(entry -> new RootPath(entry)).collect(Collectors.toList());
    }

    public String getPath(String pathId) {
        return config.get(CONFIG_ROOT_PATH_IDENTIFIER  + "pathId");
    }

}

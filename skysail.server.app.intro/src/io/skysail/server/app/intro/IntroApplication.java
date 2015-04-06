package io.skysail.server.app.intro;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.intro.topics.TopicsResource;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class IntroApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "intro";

    public IntroApplication() {
        super(APP_NAME);
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", TopicsResource.class));
        router.attach(new RouteBuilder("/", TopicsResource.class));
        router.attach(new RouteBuilder("/topics", TopicsResource.class));
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Intro", "/intro");
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }
}

package io.skysail.server.app.loop;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class LoopApplication extends LoopApplicationGen implements ApplicationProvider, MenuItemProvider {

    public  LoopApplication() {
        super("Loop", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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
        router.attach(new RouteBuilder("/weather", io.skysail.server.app.loop.entry.resources.WeatherTestResource.class));
        router.attach(new RouteBuilder("/cal", io.skysail.server.app.loop.entry.resources.CalTestResource.class));
    }

}
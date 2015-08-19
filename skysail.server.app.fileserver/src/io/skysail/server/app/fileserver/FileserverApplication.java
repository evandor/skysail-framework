package io.skysail.server.app.fileserver;

import io.skysail.server.app.SkysailApplication;

import java.util.List;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.*;


public class FileserverApplication extends SkysailApplication  implements ApplicationProvider, MenuItemProvider {

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", ListFilesResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        return null;
    }

}

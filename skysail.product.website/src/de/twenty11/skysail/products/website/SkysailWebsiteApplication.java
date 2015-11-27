package de.twenty11.skysail.products.website;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;
import io.skysail.server.utils.*;

import java.util.*;

import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;


@aQute.bnd.annotation.component.Component(immediate = true)
public class SkysailWebsiteApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "website";

    public SkysailWebsiteApplication() {
         super(APP_NAME);
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("",  WebsiteRootResource.class).noAuthenticationNeeded());
    }

    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/website/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        router = new SkysailRouter(this);
        router.attachDefault(staticDirectory);

        attach();

        return router;
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }
}

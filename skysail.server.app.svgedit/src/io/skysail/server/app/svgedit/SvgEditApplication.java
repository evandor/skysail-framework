package io.skysail.server.app.svgedit;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;
import io.skysail.server.utils.*;

import java.util.*;

import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component
// http://localhost:2015/svgedit/svg-editor.html
public class SvgEditApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "svgedit";

    public SvgEditApplication() {
        this("webapp serving static files");
    }

    public SvgEditApplication(String staticPathTemplate) {
        super(APP_NAME);
        setName("svgedit");
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", SvgEditResource.class));

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/svgedit/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        router.attachDefault(staticDirectory);
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

package io.skysail.client.todos.angularjs;

import java.util.Arrays;
import java.util.List;

import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.utils.ClassLoaderDirectory;
import de.twenty11.skysail.server.utils.CompositeClassLoader;

@Component
public class ClipboardClient extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private DefaultRedirector defaultRedirector;

    public ClipboardClient() {
        super("clipboardclient/");
        setName("clipboardclient");
        defaultRedirector = new DefaultRedirector(getContext(), "index.html");
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/note.png");
    }

    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/static/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        Router router = new Router(getContext());
        router.attachDefault(staticDirectory);
        router.attach("", defaultRedirector);
        router.attach("/", defaultRedirector);
        return router;
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Clipboard Client", "/clipboardclient/", this);
        appMenu.setCategory(MenuItem.Category.FRONTENDS_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

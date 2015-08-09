package io.skysail.client.todos.angularjs;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.utils.*;

import java.util.*;

import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.services.*;

@Component
public class TodosClient extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "TodosClient";

    private volatile DefaultRedirector defaultRedirector;

    public TodosClient() {
        super(APP_NAME + "/");
        setName(APP_NAME);
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
        MenuItem appMenu = new MenuItem("Todos Client", "/"+APP_NAME+"/" + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.FRONTENDS_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}

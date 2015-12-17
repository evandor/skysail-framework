package io.skysail.server.designer.checklist;

import io.skysail.api.repos.*;
import io.skysail.server.app.*;
import io.skysail.domain.core.Repositories;
import io.skysail.server.menus.*;

import java.util.*;
import org.osgi.service.component.annotations.*;

import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@Component(immediate = true)
public class ChecklistApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Checklist";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public ChecklistApplication() {
        super("Checklist", new ApiVersion(1), Arrays.asList());
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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
        router.attach(new RouteBuilder("/io.skysail.server.designer.checklist.Lists/{id}", io.skysail.server.designer.checklist.ListResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.designer.checklist.Lists/", io.skysail.server.designer.checklist.PostListResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.designer.checklist.Lists/{id}/", io.skysail.server.designer.checklist.PutListResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.designer.checklist.Lists", io.skysail.server.designer.checklist.ListsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.checklist.ListsResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
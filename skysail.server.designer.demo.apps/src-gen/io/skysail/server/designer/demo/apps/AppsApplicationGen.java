package io.skysail.server.designer.demo.apps;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class AppsApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Apps";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public AppsApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
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
        router.attach(new RouteBuilder("/DemoApplications/{id}", io.skysail.server.designer.demo.apps.demoapplication.resources.DemoApplicationResourceGen.class));
        router.attach(new RouteBuilder("/DemoApplications/", io.skysail.server.designer.demo.apps.demoapplication.resources.PostDemoApplicationResourceGen.class));
        router.attach(new RouteBuilder("/DemoApplications/{id}/", io.skysail.server.designer.demo.apps.demoapplication.resources.PutDemoApplicationResourceGen.class));
        router.attach(new RouteBuilder("/DemoApplications", io.skysail.server.designer.demo.apps.demoapplication.resources.DemoApplicationsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.apps.demoapplication.resources.DemoApplicationsResourceGen.class));
        router.attach(new RouteBuilder("/DemoApplications/{id}/DemoValueObjects", io.skysail.server.designer.demo.apps.demoapplication.DemoApplicationsDemoValueObjectsResource.class));
        router.attach(new RouteBuilder("/DemoApplications/{id}/DemoValueObjects/", io.skysail.server.designer.demo.apps.demoapplication.PostDemoApplicationToNewDemoValueObjectRelationResource.class));
        router.attach(new RouteBuilder("/DemoApplications/{id}/DemoValueObjects/{targetId}", io.skysail.server.designer.demo.apps.demoapplication.DemoApplicationsDemoValueObjectResource.class));
        router.attach(new RouteBuilder("/DemoValueObjects/{id}", io.skysail.server.designer.demo.apps.demovalueobject.resources.DemoValueObjectResourceGen.class));
        router.attach(new RouteBuilder("/DemoValueObjects/", io.skysail.server.designer.demo.apps.demovalueobject.resources.PostDemoValueObjectResourceGen.class));
        router.attach(new RouteBuilder("/DemoValueObjects/{id}/", io.skysail.server.designer.demo.apps.demovalueobject.resources.PutDemoValueObjectResourceGen.class));
        router.attach(new RouteBuilder("/DemoValueObjects", io.skysail.server.designer.demo.apps.demovalueobject.resources.DemoValueObjectsResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}
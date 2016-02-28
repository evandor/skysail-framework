package io.skysail.server.app.whatAbout;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class WhatAboutApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "WhatAbout";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public WhatAboutApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
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
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources/{id}", io.skysail.server.app.whatAbout.SourceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources/", io.skysail.server.app.whatAbout.PostSourceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources/{id}/", io.skysail.server.app.whatAbout.PutSourceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources", io.skysail.server.app.whatAbout.SourcesResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.whatAbout.SourcesResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}